package com.pigic.hzeropigic.utils;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.pool.PoolStats;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * HTTP连接
 * @date: 2019/3/16
 * @author guchang.pan@hand-china.com
 * @version: 0.0.1
 * @copyright Copyright (c) 2019, Hand
 */
public class HttpClientUtils {

	private final static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

	private static final String DEFAULT_CHARSET = "GBK";

	private static final String SSL_DEFAULT_SCHEME = "https";

	private static final int SSL_DEFAULT_PORT = 443;

	private static final int maxConnectCnt = 400;

	private static final int maxPreRouteConnectCnt = 50;

	private static HttpParams httpParams = new BasicHttpParams();

	private static PoolingClientConnectionManager manager = new PoolingClientConnectionManager();

	// 因为线程安全,所以加入池
	private static Map<String, DefaultHttpClient> httpClientPool = new HashMap<String, DefaultHttpClient>();

	// 超时时间设置为15s原来为30s
	private static final int TIMEOUT = 15000;

	private static final ThreadLocal<String> responseCharsetThreadLocal = new ThreadLocal<String>();

	static {

		// 设置连接线程池属性
		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);

		HttpProtocolParams.setUseExpectContinue(httpParams, Boolean.FALSE);
		HttpProtocolParams.setUserAgent(httpParams, "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");

		// 链接超时
		HttpConnectionParams.setConnectionTimeout(httpParams, HttpClientUtils.TIMEOUT);

		// 请求超时
		HttpConnectionParams.setSoTimeout(httpParams, HttpClientUtils.TIMEOUT);

		// 设置连接池最大连接数
		manager.setMaxTotal(maxConnectCnt);

		// 设置每个路由最大连接数
		// 这个参数的默认值为2，如果不设置这个参数值默认情况下对于同一个目标机器的最大并发连接只有2个
		manager.setDefaultMaxPerRoute(maxPreRouteConnectCnt);
	}

	// 异常自动恢复处理, 使用HttpRequestRetryHandler接口实现请求的异常恢复
	private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {

		// 自定义的恢复策略
		public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {

			// 设置恢复策略，在发生异常时候将自动重试3次
			if (executionCount >= 3) {
				return false;
			}

			if (exception instanceof NoHttpResponseException) {
				// Retry if the server dropped connection on us
				return true;
			}

			if (exception instanceof SSLHandshakeException) {
				// Do not retry on SSL handshake exception
				return false;
			}

			HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
			boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
			if (!idempotent) {
				// Retry if the request is considered idempotent
				return true;
			}
			return false;
		}
	};

	// 使用ResponseHandler接口处理响应，HttpClient使用ResponseHandler会自动管理连接的释放，解决了对连接的释放管理
	private static ResponseHandler<byte[]> responseHandler = new ResponseHandler<byte[]>() {

		// 自定义响应处理
		@SuppressWarnings("deprecation")
		public byte[] handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
			HttpEntity entity = response.getEntity();
			if (entity != null) {

				String charset = EntityUtils.getContentCharSet(entity) == null ? DEFAULT_CHARSET : EntityUtils.getContentCharSet(entity);

				responseCharsetThreadLocal.set(charset);

				// 如果response启用了gzip编码，则使用gzip先解码
				if (String.valueOf(response.getFirstHeader("Content-Encoding")).toLowerCase().indexOf("gzip") > -1) {
					entity = new GzipDecompressingEntity(entity);
				}

				return EntityUtils.toByteArray(entity);
			} else {
				return null;
			}
		}
	};

	/**
	 * Get方式提交,URL中不包含查询参数, 格式：http://www.g.cn
	 * 
	 * @param url
	 *            提交地址
	 * @param params
	 *            查询参数集, 键/值对
	 * @param requestCharset
	 *            参数提交编码集
	 * @return 响应消息
	 */
	public static byte[] getContent(String url, Map<String, String> params, String requestCharset) {
		if (url == null || StringUtils.isEmpty(url)) {
			return null;
		}
		List<NameValuePair> qparams = getParamsList(params);
		if (qparams != null && qparams.size() > 0) {
			requestCharset = (requestCharset == null ? DEFAULT_CHARSET : requestCharset);
			String formatParams = URLEncodedUtils.format(qparams, requestCharset);
			url = (url.indexOf("?")) < 0 ? (url + "?" + formatParams) : (url.substring(0, url.indexOf("?") + 1) + formatParams);
		}
		DefaultHttpClient httpclient = getHttpClient(requestCharset, url.indexOf("https") == 0);

		HttpGet hg = new HttpGet(url);

		// 发送请求，得到响应
		byte[] responseByte = null;
		try {
			responseByte = httpclient.execute(hg, responseHandler);
		} catch (ClientProtocolException e) {
			throw new CommonException("客户端连接协议错误", e);
		} catch (IOException e) {
			throw new CommonException("IO操作异常,查看是否超过请求设定时间:" + TIMEOUT + "毫秒!", e);
		} finally {
			abortConnection(hg, httpclient);
		}
		return responseByte;
	}

	// TODO url里面有参数的话，需要对参数进行URLEncoder
	public static byte[] getContent(String url) {
		return HttpClientUtils.getContent(url, null, null);
	}

	public static byte[] getContent(String url, Map<String, String> params) {
		return HttpClientUtils.getContent(url, null, null);
	}

	/**
	 * Get方式提交,URL中不包含查询参数, 格式：http://www.g.cn
	 * 
	 * @param url
	 *            提交地址
	 * @param params
	 *            查询参数集, 键/值对
	 * @param requestCharset
	 *            参数提交编码集
	 * @return 响应消息
	 */
	public static String get(String url, Map<String, String> params, String requestCharset, String responseCharset) {

		// 发送请求，得到响应
		String responseStr = null;
		try {
			byte[] responseByte = HttpClientUtils.getContent(url, params, requestCharset);
			responseStr = new String(responseByte, StringUtils.isBlank(responseCharset) ? responseCharsetThreadLocal.get()
					: responseCharset);
		} catch (IOException e) {
			throw new CommonException("IO操作异常,查看是否超过请求设定时间:" + TIMEOUT + "毫秒!", e);
		}

		return responseStr;
	}

	public static String get(String url) {
		return get(url, null, null, null);
	}

	public static String get(String url, Map<String, String> params) {
		return get(url, params, null, null);
	}

	public static String get(String url, Map<String, String> params, String requestCharset) {
		return get(url, params, requestCharset, null);
	}

	/**
	 * Post方式提交,URL中不包含提交参数, 格式：http://www.g.cn
	 * 
	 * @param url
	 *            提交地址
	 * @param params
	 *            提交参数集, 键/值对
	 * @param requestCharset
	 *            参数提交编码集
	 * @return 响应消息
	 */
	public static String post(String url, Map<String, String> params, String requestCharset, String responseCharset, Header... headers) {
		if (url == null || StringUtils.isEmpty(url)) {
			return null;
		}
		// 创建HttpClient实例
		DefaultHttpClient httpclient = getHttpClient(requestCharset, url.indexOf("https") == 0);
		UrlEncodedFormEntity formEntity = null;
		try {
			if (requestCharset == null || StringUtils.isEmpty(requestCharset)) {
				formEntity = new UrlEncodedFormEntity(getParamsList(params));
			} else {
				formEntity = new UrlEncodedFormEntity(getParamsList(params), requestCharset);
			}
		} catch (UnsupportedEncodingException e) {
			throw new CommonException("不支持的编码集", e);
		}
		HttpPost hp = new HttpPost(url);
        TempUtils.getContainer().put("postBody",params);
		hp.setEntity(formEntity);
		if (headers != null && headers.length > 0) {
			for (Header header : headers) {
				if (hp.getFirstHeader(header.getName()) != null) {
					hp.setHeader(header);
				} else {
					hp.addHeader(header);
				}
			}
		}

		hp.getAllHeaders();

		// 发送请求，得到响应
		String responseStr = null;
		try {
			byte[] responseByte = httpclient.execute(hp, responseHandler);
			responseStr = new String(responseByte, StringUtils.isBlank(responseCharset) ? responseCharsetThreadLocal.get()
					: responseCharset);
		} catch (ClientProtocolException e) {
			logger.error(e.getMessage(), e);
			throw new CommonException("客户端连接协议错误", e);
		} catch (IOException e) {
			throw new CommonException("IO操作异常,查看是否超过请求设定时间:" + TIMEOUT + "毫秒!", e);
		} finally {
			abortConnection(hp, httpclient);
		}
		return responseStr;
	}

	public static String post(String url, Map<String, String> params, String requestCharset) {
		return post(url, params, requestCharset, null);
	}

	public static String post(String url) {
		return post(url, null);
	}

	public static String post(String url, Map<String, String> params) {
		return post(url, params, null, null);
	}

	/**
	 * 文件上传
	 * 
	 * @param url
	 * @param params
	 * @param isMultipart
	 *            true:文件上传,false:普通
	 * @return
	 */
	public static String post(String url, Map<String, String> params, boolean isMultipart) {
		return post(url, null);
	}

	public static String post(String url, final String body, final String charset, String responseCharset, Header... headers) {
		if (url == null || StringUtils.isEmpty(url)) {
			return null;
		}
		// 创建HttpClient实例
		DefaultHttpClient httpclient = getHttpClient(charset, url.indexOf("https") == 0);
		StringEntity entity = null;
		try {
			entity = new StringEntity(body, charset);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		TempUtils.getContainer().put("postBody",body);
		HttpPost hp = new HttpPost(url);
		hp.setEntity(entity);
		if (headers != null && headers.length > 0) {
			for (Header header : headers) {
				if (hp.getFirstHeader(header.getName()) != null) {
					hp.setHeader(header);
				} else {
					hp.addHeader(header);
				}
			}
		}

		// 发送请求，得到响应
		String responseStr = null;
		try {
			byte[] responseByte = httpclient.execute(hp, responseHandler);
			responseStr = new String(responseByte, StringUtils.isBlank(responseCharset) ? responseCharsetThreadLocal.get()
					: responseCharset);
		} catch (ClientProtocolException e) {
			throw new CommonException("客户端连接协议错误", e);
		} catch (IOException e) {
			throw new CommonException("IO操作异常", e);
		} finally {
			abortConnection(hp, httpclient);
		}
		return responseStr;
	}

	/**
	 * Post方式提交,忽略URL中包含的参数,解决SSL双向数字证书认证
	 * 
	 * @param url
	 *            提交地址
	 * @param params
	 *            提交参数集, 键/值对
	 * @param charset
	 *            参数编码集
	 * @param keystoreUrl
	 *            密钥存储库路径
	 * @param keystorePassword
	 *            密钥存储库访问密码
	 * @param truststoreUrl
	 *            信任存储库绝路径
	 * @param truststorePassword
	 *            信任存储库访问密码, 可为null
	 * @return 响应消息
	 * @throws NetServiceException
	 */
	public static String post(String url, Map<String, String> params, String charset, final URL keystoreUrl, final String keystorePassword,
			final URL truststoreUrl, final String truststorePassword, String responseCharset) {
		if (url == null || StringUtils.isEmpty(url)) {
			return null;
		}
		DefaultHttpClient httpclient = getHttpClient(charset, keystoreUrl, keystorePassword, truststoreUrl, truststorePassword,
				url.indexOf("https") == 0);
		UrlEncodedFormEntity formEntity = null;
		try {
			if (charset == null || StringUtils.isEmpty(charset)) {
				formEntity = new UrlEncodedFormEntity(getParamsList(params));
			} else {
				formEntity = new UrlEncodedFormEntity(getParamsList(params), charset);
			}
		} catch (UnsupportedEncodingException e) {
			throw new CommonException("不支持的编码集", e);
		}
		HttpPost hp = null;
		String responseStr = null;
		try {
			hp = new HttpPost(url);
			TempUtils.getContainer().put("postBody",params);
			hp.setEntity(formEntity);
			byte[] responseByte = httpclient.execute(hp, responseHandler);
			responseStr = new String(responseByte, StringUtils.isBlank(responseCharset) ? responseCharsetThreadLocal.get()
					: responseCharset);
		} catch (FileNotFoundException e) {
			throw new CommonException("keystore文件不存在", e);
		} catch (IOException e) {
			throw new CommonException("I/O操作失败或中断 ", e);
		} finally {
			abortConnection(hp, httpclient);
		}
		return responseStr;
	}


	

	private static DefaultHttpClient getHttpClient(final String charset, boolean isHttps) {
		return getHttpClient(charset, null, null, null, null, isHttps);
	}

	@SuppressWarnings("deprecation")
	private static DefaultHttpClient getHttpClient(final String charset, final URL keystoreUrl, final String keystorePassword,
			final URL truststoreUrl, final String truststorePassword, final boolean isHttps) {

		String httpCilentName = "default";

		if (keystoreUrl != null) {
			httpCilentName = keystoreUrl.toString();
		}

		if (isHttps) {
			httpCilentName = "default-https";
		}

		// 加锁
		synchronized (httpClientPool) {
			if (!httpClientPool.containsKey(httpCilentName)) {

				// 设置字符集
				HttpProtocolParams.setContentCharset(httpParams, charset == null ? DEFAULT_CHARSET : charset);

				// 使用连接池创建连接
				DefaultHttpClient httpclient = new DefaultHttpClient(manager, httpParams);

				httpclient.setHttpRequestRetryHandler(requestRetryHandler);

				// 默认启用压缩
				httpclient.addRequestInterceptor(new HttpRequestInterceptor() {

					public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
						if (!request.containsHeader("Accept-Encoding")) {
							request.addHeader("Accept-Encoding", "gzip");
						}
					}
				});
				//Update By PanguChang
				HttpResponseInterceptor httpResponseInterceptor = SpringUtils.getBean(HttpResponseInterceptor.class);
				httpclient.addResponseInterceptor(httpResponseInterceptor);
				// 设置httpclient证书
				if (keystoreUrl != null) {

					KeyStore keyStore;
					KeyStore trustStore;
					SSLSocketFactory socketFactory;
					try {
						keyStore = createKeyStore(keystoreUrl, keystorePassword);
						trustStore = createKeyStore(truststoreUrl, keystorePassword);
						socketFactory = new SSLSocketFactory(keyStore, keystorePassword, trustStore);

						Scheme scheme = new Scheme(SSL_DEFAULT_SCHEME, socketFactory, SSL_DEFAULT_PORT);
						httpclient.getConnectionManager().getSchemeRegistry().register(scheme);
					} catch (KeyStoreException e) {
						throw new CommonException("keytore解析异常", e);
					} catch (NoSuchAlgorithmException e) {
						throw new CommonException("指定的加密算法不可用", e);
					} catch (CertificateException e) {
						throw new CommonException("信任证书过期或解析异常", e);
					} catch (IOException e) {
						throw new CommonException("I/O操作失败或中断 ", e);
					} catch (KeyManagementException e) {
						throw new CommonException("处理密钥管理的操作异常", e);
					} catch (UnrecoverableKeyException e) {
						throw new CommonException("keystore中的密钥无法恢复异常", e);
					}
				} else if (isHttps) {

					// 如果是https访问
					SSLContext ctx = null;
					try {
						ctx = SSLContext.getInstance("TLS");
						ctx.init(null, new TrustManager[] { new X509TrustManager() {
							public java.security.cert.X509Certificate[] getAcceptedIssuers() {
								return null;
							}

							@SuppressWarnings("unused")
							public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
							}

							@SuppressWarnings("unused")
							public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
							}

							public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
									throws CertificateException {
							}

							public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
									throws CertificateException {
							}
						} }, null);
					} catch (Exception e) {
						throw new CommonException(e.getMessage(), e);
					}

					SSLSocketFactory socketFactory = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

					Scheme scheme = new Scheme(SSL_DEFAULT_SCHEME, socketFactory, SSL_DEFAULT_PORT);
					httpclient.getConnectionManager().getSchemeRegistry().register(scheme);
				}
				httpClientPool.put(httpCilentName, httpclient);
			}
		}
		
		PoolStats poolStats = manager.getTotalStats();

		logger.debug("当前httpclient连接池信息-总连接数:{},正在执行数:{},空闲连接数:{},阻塞连接数:{}", new Object[] { poolStats.getMax(), poolStats.getLeased(),
				poolStats.getAvailable(), poolStats.getPending() });

		return httpClientPool.get(httpCilentName);
	}

	/**
	 * 释放HttpClient连接
	 * 
	 * @param hrb
	 *            请求对象
	 * @param httpclient
	 *            client对象
	 */
	private static void abortConnection(final HttpRequestBase hrb, final HttpClient httpclient) {

		if (hrb != null) {
			hrb.abort();
		}

		if (httpclient != null && httpclient.getConnectionManager() != null && !httpClientPool.containsValue(httpclient)) {
			httpclient.getConnectionManager().shutdown();
		}
	}

	/**
	 * 从给定的路径中加载此 KeyStore
	 * 
	 * @param url
	 *            keystore URL路径
	 * @param password
	 *            keystore访问密钥
	 * @return keystore 对象
	 */
	private static KeyStore createKeyStore(final URL url, final String password) throws KeyStoreException, NoSuchAlgorithmException,
			CertificateException, IOException {
		if (url == null) {
			throw new IllegalArgumentException("Keystore url may not be null");
		}
		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		InputStream is = null;
		try {
			is = url.openStream();
			keystore.load(is, password != null ? password.toCharArray() : null);
		} finally {
			if (is != null) {
				is.close();
				is = null;
			}
		}
		return keystore;
	}

	/**
	 * 将传入的键/值对参数转换为NameValuePair参数集
	 * 
	 * @param paramsMap
	 *            参数集, 键/值对
	 * @return NameValuePair参数集
	 */
	private static List<NameValuePair> getParamsList(Map<String, String> paramsMap) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		if (paramsMap != null && paramsMap.size() > 0) {
			for (Map.Entry<String, String> map : paramsMap.entrySet()) {
				params.add(new BasicNameValuePair(map.getKey(), map.getValue()));
			}
		}

		return params;
	}

	public static void main(String[] args) throws Exception {

		long startTime = System.currentTimeMillis();

		for (int i = 0; i < 1000; i++) {
			HttpClientUtils.get("https://open.ccchong.com/");
		}

		System.out.print(System.currentTimeMillis() - startTime);

	}
}
