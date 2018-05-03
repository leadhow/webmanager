package datamanager.module.search;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import datamanager.module.search.dto.SearchResult;
import datamanager.module.search.dto.Webpage;
import datamanager.module.search.searchengine.baidu.JSoupBaiduSearcher;

public class TestJSoupBaiduSearcher {

	private static final Logger LOG = LoggerFactory.getLogger(TestJSoupBaiduSearcher.class);

	@Test
	public void TestJSoupBaiduSearcher() {
		ISearcher searcher = new JSoupBaiduSearcher();
		SearchResult searchResult = searcher.search("海康互联网", 1);
		List<Webpage> webpages = searchResult.getWebpages();
		if (webpages != null) {
			int i = 1;
			LOG.info("搜索结果 当前第 " + searchResult.getPage() + " 页，页面大小为：" + searchResult.getPageSize() + " 共有结果数：" + searchResult.getTotal());
			for (Webpage webpage : webpages) {
				LOG.info("搜索结果 " + (i++) + " ：");
				LOG.info("标题：" + webpage.getTitle());
				LOG.info("URL：" + webpage.getUrl());
				LOG.info("摘要：" + webpage.getSummary());
				LOG.info("正文：" + webpage.getContent());
				LOG.info("");
			}
		} else {
			LOG.error("没有搜索到结果");
		}
	}

}
