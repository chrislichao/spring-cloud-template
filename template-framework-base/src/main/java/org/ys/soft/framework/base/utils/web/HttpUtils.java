package org.ys.soft.framework.base.utils.web;

import java.io.ByteArrayInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * [Http请求的工具类]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
@SuppressWarnings("deprecation")
public class HttpUtils {

	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	/**
	 * [发送contentType=x-www-form-urlencoded的POST请求]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String httpFormUrlEncodedPost(String url, String content, boolean needSSL) {
		return httpPost(url, ContentType.APPLICATION_FORM_URLENCODED, null, content, needSSL);
	}

	/**
	 * [发送contentType=json的POST请求]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String httpJsonPost(String url, String content, boolean needSSL) {
		return httpPost(url, ContentType.APPLICATION_JSON, null, content, needSSL);
	}

	/**
	 * [发送contentType=json的POST请求,并额外添加请求头信息]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String httpJsonPost(String url, Map<String, String> headerMap, String content, boolean needSSL) {
		return httpPost(url, ContentType.APPLICATION_JSON, headerMap, content, needSSL);
	}

	/**
	 * [基础的Http Post请求方法]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private static String httpPost(String url, ContentType contentType, Map<String, String> headerMap, String content, boolean needSSL) {
		// 创建HttpPost
		String result = null;
		HttpClient httpClient = getHttpClient(needSSL, getHostFromURL(url));
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", contentType + ";charset=utf-8");
			if (headerMap != null && CollectionUtils.isNotEmpty(headerMap.keySet())) {
				for (String key : headerMap.keySet()) {
					httpPost.setHeader(key, headerMap.get(key));
				}
			}
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
			httpPost.setConfig(requestConfig);
			BasicHttpEntity requestBody = new BasicHttpEntity();
			requestBody.setContent(new ByteArrayInputStream(content.getBytes("utf-8")));
			requestBody.setContentLength(content.getBytes("utf-8").length);
			httpPost.setEntity(requestBody);
			// 执行客户端请求
			HttpEntity entity = httpClient.execute(httpPost).getEntity();

			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
				EntityUtils.consume(entity);
			}

		} catch (Throwable e) {
			logger.error("【HTTP请求失败】: url={}, content={}", url, content);
		}

		return result;
	}

	/**
	 * [获取HttpClient]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private static DefaultHttpClient getHttpClient(boolean sslClient, String host) {
		DefaultHttpClient httpclient = null;
		if (sslClient) {
			try {
				SSLHttpClient chc = new SSLHttpClient();
				InetAddress address = null;
				String ip;
				try {
					address = InetAddress.getByName(host);
					ip = address.getHostAddress().toString();
					httpclient = chc.registerSSL(ip, "TLS", 443, "https");
				} catch (UnknownHostException e) {
					logger.error("获取请求服务器地址失败：host = {} " + host);
					e.getStackTrace().toString();
				}
				HttpParams hParams = new BasicHttpParams();
				hParams.setParameter("https.protocols", "SSLv3,SSLv2Hello");
				httpclient.setParams(hParams);
			} catch (KeyManagementException e) {
				logger.error(e.getStackTrace().toString());
			} catch (NoSuchAlgorithmException e) {
				logger.error(e.getStackTrace().toString());
			}
		} else {
			httpclient = new DefaultHttpClient();
		}
		return httpclient;
	}

	/**
	 * [从url中获取主机名]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private static String getHostFromURL(String url) {
		String host = "";
		Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
		Matcher matcher = p.matcher(url);
		if (matcher.find()) {
			host = matcher.group();
		}
		return host;
	}

	public static void main(String[] args) throws UnknownHostException {

		System.out.println(InetAddress.getByName("api.ucpaas.com").getHostAddress().toString());
	}
}
