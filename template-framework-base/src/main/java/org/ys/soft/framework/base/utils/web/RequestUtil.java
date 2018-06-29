package org.ys.soft.framework.base.utils.web;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.ys.soft.framework.base.utils.Assert;

/**
 * [请求的工具类]
 * 
 * @author Chris li[黎超]
 * @version [版本,2016-10-10]
 * @see
 */
public class RequestUtil {
	private static final Logger logger = LoggerFactory.getLogger(RequestUtil.class);
	private static final Base64 base64 = new Base64(true);
	public static final String LAST_PAGE = "com.alibaba.lastPage";
	public static final String REDIRECT_HOME = "/";

	/**
	 * [获取当前Request对象.]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本,2016-10-10]
	 */
	public static HttpServletRequest currentRequest() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		Assert.notNull(attrs, "当前线程中不存在 Request上下文!");
		return attrs.getRequest();
	}

	/**
	 * [获取当前session对象]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static HttpSession currentSession() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attrs != null ? attrs.getRequest().getSession(false) : null;
	}

	/**
	 * [保存当前请求]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void saveRequest() {
		HttpServletRequest request = currentRequest();
		request.getSession().setAttribute(LAST_PAGE, RequestUtil.getPageAndParam(request));
		logger.debug("save request for {}", request.getRequestURI());
	}

	/**
	 * [加密请求页面]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String hashRequestPage(HttpServletRequest request) {
		String reqUri = request.getRequestURI();
		String query = request.getQueryString();
		if (query != null) {
			reqUri += "?" + query;
		}
		String targetPage = null;
		try {
			targetPage = base64.encodeAsString(reqUri.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException ex) {

		}
		return targetPage;
	}

	/**
	 * [获得带参数的请求路径(GET和 POST都可用)]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String getPageAndParam(HttpServletRequest request) {
		Map<String, String[]> params = request.getParameterMap();
		String reqUri = request.getRequestURI();
		if (!params.isEmpty()) {
			String queryString = "";
			for (String key : params.keySet()) {
				String[] values = params.get(key);
				for (int i = 0; i < values.length; i++) {
					String value = values[i];
					queryString += key + "=" + value + "&";
				}
			}
			// 去掉最后一个空格
			queryString = queryString.substring(0, queryString.length() - 1);
			reqUri += "?" + queryString;
			logger.info(reqUri);
		}
		String targetPage = null;
		try {
			targetPage = base64.encodeAsString(reqUri.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException ex) {

		}
		return targetPage;
	}

	/**
	 * [取出之前保存的请求]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String retrieveSavedRequest() {
		HttpSession session = currentSession();
		if (session == null) {
			return REDIRECT_HOME;
		}
		String HashedlastPage = (String) session.getAttribute(LAST_PAGE);
		if (HashedlastPage == null) {
			return REDIRECT_HOME;
		} else {
			return retrieve(HashedlastPage);
		}
	}

	/**
	 * [解密请求的页面]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String retrieve(String targetPage) {
		byte[] decode = base64.decode(targetPage);
		try {
			String requestUri = new String(decode, "UTF-8");
			int i = requestUri.indexOf("/", 1);
			return requestUri.substring(i);
		} catch (UnsupportedEncodingException ex) {
			return null;
		}
	}

	/**
	 * [获取基础路径]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String getBasePath(HttpServletRequest request) {
		// 放入项目根路径
		String path = request.getContextPath();
		String server = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		return server + path;
	}

	/**
	 * [获取客户端真实IP地址]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * [获取请求中指定的的Cookie值]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieKey) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(cookieKey)) {
				return cookie.getValue();
			}
		}
		return null;
	}

	/**
	 * [获取请求中的token值]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String getToken(HttpServletRequest request, String cookieKey) {
		String token = getCookieValue(request, cookieKey);
		if (StringUtils.isBlank(token)) {
			token = request.getHeader("Authorization");
		}
		return token;
	}
}
