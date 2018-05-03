package datamanager.module.search.searchengine.baidu;

import datamanager.module.search.dto.SearchResult;

public abstract class AbstractBaiduSearcher implements IBaiduSearcher {

	@Override
	public SearchResult searchNews(String keyWord) {
		return searchNews(keyWord, 1);
	}

	@Override
	public SearchResult searchNews(String keyword, int page) {
		throw new RuntimeException("未实现");
	}

	@Override
	public SearchResult searchTieba(String keyword) {
		return searchTieba(keyword, 1);
	}

	@Override
	public SearchResult searchTieba(String keyword, int page) {
		throw new RuntimeException("未实现");
	}

	@Override
	public SearchResult searchZhidao(String keyword) {
		return searchZhidao(keyword, 1);
	}

	@Override
	public SearchResult searchZhidao(String keyword, int page) {
		throw new RuntimeException("未实现");
	}

	@Override
	public SearchResult searchWenku(String keyword) {
		return searchWenku(keyword, 1);
	}

	@Override
	public SearchResult searchWenku(String keyword, int page) {
		throw new RuntimeException("未实现");
	}

}
