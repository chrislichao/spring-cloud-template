package org.ys.soft.framework.base;

import java.util.HashMap;
import java.util.Map;

/**
 * [结果对象,用于Ajax请求返回使用]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public class Result {
	/**
	 * 错误编码
	 */
	private String errCode;
	/**
	 * 错误信息
	 */
	private String errMsg;
	/**
	 * 存放返回的数据结构
	 */
	private Map<String, Object> data = new HashMap<String, Object>();
	
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public void put(String key, Object obj) {
		this.data.put(key, obj);
	}

	public void setError(String errCode,String errMsg){
		setErrCode(errCode);
		setErrMsg(errMsg);
	}
}
