package datamanager.module.search;

import datamanager.module.search.dto.SearchResult;

/**
 * <p>
 * Title:Searcher
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author guoyangyang
 * @date 2018年5月2日 上午10:12:54
 */
public interface ISearcher {

	/**
	 * 依据关键字查询信息
	 * 
	 * @param keyWord
	 *            关键字
	 * @return
	 */
	SearchResult search(String keyWord);

	/**
	 * 依据关键字查询信息(分页)
	 * 
	 * @param keyWord
	 *            关键字
	 * @param page
	 * @return
	 */
	SearchResult search(String keyWord, int page);
}
