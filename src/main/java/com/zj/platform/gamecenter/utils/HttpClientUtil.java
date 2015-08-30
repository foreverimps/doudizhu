package com.zj.platform.gamecenter.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class HttpClientUtil {

	/** 获取连接超时时间 */
	private static final int CONNECT_TIMEOUT = 5000;

	private static PoolingHttpClientConnectionManager connManager = null;

	private static final String HTTP_RESULT_RETURN_CODE = "";

	private static CloseableHttpClient httpclient = null;

	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	/** HTTP保留时间 */
	private final static int MAX_HTTP_KEEP_ALIVE = 30 * 1000;

	/** 每个路由最大连接数 */
	private final static int MAX_ROUTE_CONNECTIONS = 400;

	/** 最大连接数 */
	private final static int MAX_TOTAL_CONNECTIONS = 800;

	/** 连接处理超时时间 */
	private static final int SOCKET_TIMEOUT = 20000;

	static {
		HttpRequestRetryHandler myRetryHandler = customRetryHandler();
		ConnectionKeepAliveStrategy customKeepAliveHander = customKeepAliveHander();
		connManager = new PoolingHttpClientConnectionManager();
		connManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
		connManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
		HttpHost localhost = new HttpHost("locahost", 80);
		connManager.setMaxPerRoute(new HttpRoute(localhost), 50);
		httpclient = HttpClients.custom().setConnectionManager(connManager).setKeepAliveStrategy(customKeepAliveHander)
		        .setRetryHandler(myRetryHandler).build();
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}
		return prestr;
	}

	/**
	 * 从Http调用结果中，获取结果表示码。
	 * 
	 * @param httpResult
	 * @return
	 */
	public static int getReturnCodeFromResult(String httpResult) {
		JSONObject respJson = JSON.parseObject(httpResult);
		return Integer.valueOf(respJson.getString(HTTP_RESULT_RETURN_CODE));
	}

	public static String httpGet(String url) throws Exception {
		return httpGet(url, null);
	}

	public static String httpGet(String url, Map<String, String> params) throws Exception {
		CloseableHttpResponse response = null;
		if (params != null) {
			url = url + "?" + createLinkString(params);
		}
		logger.info("get请求地址: {}", url);
		HttpGet httpGet = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
		httpGet.setConfig(requestConfig);
		String responseData = null;
		try {
			httpGet = new HttpGet(url);
			response = httpclient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("{} 接口调用失败：{}", url, response.getStatusLine());
				return null;
			}
			HttpEntity entity = response.getEntity();
			responseData = EntityUtils.toString(entity, "UTF-8");
			logger.info("{} 请求响应：{}", url, responseData);
			EntityUtils.consume(entity);
		}
		catch (Exception e) {
			logger.error("调用第三方接口异常！", e);
			logger.error(e.getMessage(), e);
			throw e;
		}
		finally {
			httpGet.releaseConnection();
			HttpClientUtils.closeQuietly(response);
		}
		return responseData;
	}

	public static String httpPost(String url, Map<String, String> paramMap) throws Exception {
		// 封装请求参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : paramMap.entrySet()) {
			nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		String responseData = null;
		CloseableHttpResponse response = null;
		HttpPost httpPost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
		httpPost.setConfig(requestConfig);
		logger.info("post请求地址：{}, 请求参数：{}", url, paramMap);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			response = httpclient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("{} 接口调用失败：{}", url, response.getStatusLine());
				return null;
			}
			HttpEntity entity = response.getEntity();
			responseData = EntityUtils.toString(entity, "utf-8");
			logger.info("{} 请求响应：{}", url, responseData);
			EntityUtils.consume(entity);
		}
		catch (Exception e) {
			logger.error("调用第三方接口异常！", e);
			logger.error(e.getMessage(), e);
			throw e;
		}
		finally {
			httpPost.releaseConnection();
			HttpClientUtils.closeQuietly(response);
		}
		return responseData;
	}

	public static String httpPost(String url, String jsonString) throws Exception {
		String responseData = null;
		CloseableHttpResponse response = null;
		HttpPost httpPost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
		httpPost.setConfig(requestConfig);
		StringEntity reqEntity = new StringEntity(jsonString, ContentType.create("application/json", "UTF-8"));
		httpPost.setEntity(reqEntity);
		logger.info("post请求地址：{}, 请求参数：{}", url, jsonString);
		try {
			response = httpclient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("{} 接口调用失败：{}", url, response.getStatusLine());
				return null;
			}
			HttpEntity entity = response.getEntity();
			responseData = EntityUtils.toString(entity, "utf-8");
			logger.info("{} 请求响应：{}", url, responseData);
			EntityUtils.consume(entity);
		}
		catch (Exception e) {
			logger.error("调用第三方接口异常！", e);
			logger.error(e.getMessage(), e);
			throw e;
		}
		finally {
			httpPost.releaseConnection();
			HttpClientUtils.closeQuietly(response);
		}
		return responseData;
	}

	/**
	 * 检查Http调用的结果是否正确。
	 * 
	 * @param httpResult
	 * @return
	 */
	public static boolean isSuccessfulHttpResult(String httpResult) {
		if (1 == getReturnCodeFromResult(httpResult)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 设置HTTP连接保留时间
	 * 
	 * @return 保留策略
	 */
	private static ConnectionKeepAliveStrategy customKeepAliveHander() {
		ConnectionKeepAliveStrategy myKeepAliveHander = new ConnectionKeepAliveStrategy() {

			@Override
			public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
				return MAX_HTTP_KEEP_ALIVE;
			}
		};
		return myKeepAliveHander;
	}

	/**
	 * 是否重试
	 * 
	 * @return false，不重试
	 */
	private static HttpRequestRetryHandler customRetryHandler() {
		HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {

			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				return false;
			}
		};
		return myRetryHandler;
	}
}
