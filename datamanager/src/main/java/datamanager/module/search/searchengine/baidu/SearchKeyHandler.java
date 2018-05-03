package datamanager.module.search.searchengine.baidu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import datamanager.module.search.dto.SearchResult;
import datamanager.module.search.dto.Webpage;

/**
 * <p>
 * Title:excelUtils
 * </p>
 * <p>
 * Description:excel文件常见操作
 * </p>
 * 
 * @author guoyangyang
 * @date 2018年5月2日 上午8:50:44
 */
public class SearchKeyHandler {

	private static Logger LOG = LoggerFactory.getLogger(SearchKeyHandler.class);
	private static String EXCEL_SUFFIX_XLSX = "xlsx";
	private static String EXCEL_SUFFIX_XLS = "xls";
	private static List<String> FAIL_SEARCH_KEY = Collections.synchronizedList(new ArrayList<String>());// 查询结果为空
	private static List<String> EMPTY_SEARCH_KEY = Collections.synchronizedList(new ArrayList<String>());// 查询结果不满足结果key
	private static String EXIST_FLAG = "1";// 查询结果包含(大于等于)关键字五条以上标志
	private static String NOT_EXIST_FLAG = "0";// 查询结果包含关键字低于(小于)以上标志
	private static Integer TOTAL_EXIST_FLAG = 5;// 查询结果包含总次数
	private static String KEY_WORD_POSSTFIX = "互联网";// 关键字后缀

	public static void main(String[] args) {
		searchMainFunction("E:\\test.xlsx", "xlsx");
	}

	/**
	 * 加载excel内容,利用keyword查询,比对结果
	 * 
	 * @param excelFilePath
	 *            excel文件内容
	 * @param fileSuffix
	 *            文件后缀名称
	 */
	public static void searchMainFunction(String excelFilePath, String fileSuffix) {
		Workbook workbook = null;
		try {
			if (EXCEL_SUFFIX_XLS.equals(fileSuffix)) {
				workbook = new HSSFWorkbook(new FileInputStream(new File(excelFilePath)));
			} else if (EXCEL_SUFFIX_XLSX.equals(fileSuffix)) {
				workbook = new XSSFWorkbook(new FileInputStream(new File(excelFilePath)));
			}
		} catch (FileNotFoundException e) {
			LOG.info("excel file is not exist:{}", e);
			return;
		} catch (IOException e) {
			LOG.info("read excel has a IOException:{}", e);
			return;
		} finally {
		}

		SearchKeyHandler searchKeyHandler = new SearchKeyHandler();
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			Sheet sheetAt = workbook.getSheetAt(i);
			for (int j = 0; j < sheetAt.getPhysicalNumberOfRows(); j++) {
				Row row = sheetAt.getRow(j);
				String name = "";
				for (int k = 0; k < row.getPhysicalNumberOfCells(); k++) {
					if (k == 1) {
						name = row.getCell(k).getStringCellValue();
						break;
					}
				}

				SearchResult searchResult = searchKeyHandler.searchContentByBaiduSearcher(name + KEY_WORD_POSSTFIX, 2, 3);
				updateExcel(searchResult, row, name);
			}
		}
	}

	/**
	 * 依据关键字利用百度引擎查询
	 * 
	 * @param keyWord
	 *            查询关键字
	 * @param page
	 *            查询几页(百度查询默认每页10条记录)
	 * @param tryTotalCount
	 *            重试查询次数
	 * @return
	 */
	public SearchResult searchContentByBaiduSearcher(String keyWord, int page, int tryTotalCount) {
		HtmlUnitSearcher htmlUnitSearcher = new HtmlUnitSearcher();
		SearchResult searchResult = null;
		int tryCount = 0;
		while (tryCount < tryTotalCount && searchResult == null) {
			try {
				searchResult = htmlUnitSearcher.search(keyWord, page);
			} catch (Exception e) {
				LOG.info("get searchResult count is:{} has a exception:{}", tryCount, e);
			}
		}
		if (searchResult == null) {
			FAIL_SEARCH_KEY.add(keyWord);
			LOG.info("get searchResult failed. key:{}", keyWord);
		}
		return searchResult;
	}

	/**
	 * 更新excel表格内容
	 * 
	 * @param searchResult
	 *            查询结果
	 * @param row
	 *            excel表格中某一行
	 */
	public static void updateExcel(SearchResult searchResult, Row row, String name) {
		List<Webpage> webpages = searchResult.getWebpages();
		int count = 0;
		for (Webpage webpage : webpages) {
			String title = webpage.getTitle();
			if (title.contains(KEY_WORD_POSSTFIX)) {
				count++;
			}
		}

		// 更新excel表格内容
		if (count >= TOTAL_EXIST_FLAG) {
			for (int k = 0; k < row.getPhysicalNumberOfCells(); k++) {
				if (k == 2) {
					row.getCell(k).setCellValue(EXIST_FLAG);
					break;
				}
			}
		} else {
			EMPTY_SEARCH_KEY.add(name);
			for (int k = 0; k < row.getPhysicalNumberOfCells(); k++) {
				if (k == 2) {
					row.getCell(k).setCellValue(NOT_EXIST_FLAG);
					break;
				}
			}
		}
	}
}
