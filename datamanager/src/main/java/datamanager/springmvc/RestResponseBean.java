package datamanager.springmvc;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**
 * <p>
 * Title:RestResponseBean
 * </p>
 * <p>
 * Description:rest接口统一返回数据格式
 * </p>
 * 
 * @author guoyangyang
 * @date 2018年4月27日 下午8:23:11
 */
public class RestResponseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String code;

	private String msg;

	private Object data;

	public RestResponseBean() {
		super();
	}

	public RestResponseBean(String code, String msg, Object data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
