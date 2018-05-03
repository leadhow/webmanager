package datamanager.module.search.dto;

import com.alibaba.fastjson.JSONObject;

/**
 * <p>
 * Title:WebPage
 * </p>
 * <p>
 * Description:网页解析实体
 * </p>
 * 
 * @author guoyangyang
 * @date 2018年5月2日 上午10:14:55
 */
public class Webpage {

	// 标题
	private String title;

	// 链接
	private String url;

	// 简介
	private String summary;

	// 正文内容
	private String content;

	public Webpage() {
		super();
	}

	public Webpage(String title, String url, String summary, String content) {
		super();
		this.title = title;
		this.url = url;
		this.summary = summary;
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
