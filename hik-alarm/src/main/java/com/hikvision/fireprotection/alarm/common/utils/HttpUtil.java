package com.hikvision.fireprotection.alarm.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http工具类
 *
 * @author wangjinchang5
 * @date 2020/12/18 22:20
 * @since 1.0.100
 */
@Slf4j
public class HttpUtil {
    private HttpUtil() {
    }

    private static final HttpClient HTTP_CLIENT;

    static final Integer DEFAULT_CONNECT_TIMEOUT = 15000;

    static final String DEFAULT_CHARSET = "UTF-8";

    static final String DEFAULT_HTTPS = "https";

    static {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(128);

        HTTP_CLIENT = HttpClients.custom().setConnectionManager(connectionManager).build();
    }

    private static CloseableHttpClient buildCloseableHttpClient() throws GeneralSecurityException {
        SSLContext sslContext =
                new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build();
        SSLConnectionSocketFactory socketFactory =
                new SSLConnectionSocketFactory(sslContext, (hostname, session) -> true);

        return HttpClients.custom().setSSLSocketFactory(socketFactory).build();
    }

    public static String executePost(String url, String jsonEntity, Map<String, String> requestHeader) {
        try {
            String response = httpPostToString(url, jsonEntity, requestHeader);
            if (log.isDebugEnabled()) {
                log.debug("Execute post request successful.The parameter is {},response is {}.", jsonEntity, response);
            }
            return response;
        } catch (IOException e) {
            log.error("Fail to execute post request.The url is {},the parameter is {}.", url, jsonEntity);
            return null;
        }
    }

    public static String executePost(String url, Map<String, String> parameter,
                                     String jsonEntity, Map<String, String> requestHeader) {
        return executePost(spliceUrlWithParam(url, parameter), jsonEntity, requestHeader);
    }

    private static String httpPostToString(String url, String jsonEntity,
                                           Map<String, String> requestHeader) throws IOException {
        // 1 创建HttpPost
        HttpPost httpPost = new HttpPost(url);
        // 2 定制HttpConfig
        httpPost.setConfig(customRequestConfigBuilder().build());
        // 3 设置HttpHeader
        setHeader(httpPost, requestHeader);
        // 4 设置请求参数
        if (StringUtils.isNotEmpty(jsonEntity)) {
            StringEntity entity = new StringEntity(jsonEntity, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
        }

        return executeHttpRequest(url.startsWith(DEFAULT_HTTPS), httpPost);
    }

    public static String executeGet(String url, Map<String, String> requestHeader, Map<String, String> parameter) {
        try {
            String response = httpGetToString(url, requestHeader, parameter);
            if (log.isDebugEnabled()) {
                log.debug("Execute get request successful.The parameter is {},response is {}.", parameter, response);
            }
            return response;
        } catch (IOException e) {
            log.error("Fail to execute get request.The url is {},the parameter is {}.", url, parameter);
            return null;
        }
    }

    public static String executeGet(String url, Map<String, String> requestHeader) {
        return executeGet(url, requestHeader, null);
    }

    private static String httpGetToString(String url,
                                          Map<String, String> requestHeader,
                                          Map<String, String> parameter) throws IOException {
        // 1 拼接URL（设置请求参数）
        url = spliceUrlWithParam(url, parameter);
        // 2 创建HttpGet
        HttpGet httpGet = new HttpGet(url);
        // 3 定制HttpConfig
        httpGet.setConfig(customRequestConfigBuilder().build());
        // 4 设置HttpHeader
        setHeader(httpGet, requestHeader);

        return executeHttpRequest(url.startsWith(DEFAULT_HTTPS), httpGet);
    }

    private static String spliceUrlWithParam(String url, Map<String, String> parameter) {
        String splitJoint;
        if (MapUtils.isNotEmpty(parameter)) {
            List<String> param = new ArrayList<>(parameter.size());
            for (Map.Entry<String, String> entry : parameter.entrySet()) {
                param.add(entry.getKey() + "=" + entry.getValue());
            }
            String parameterString = StringUtils.join(param, "&");

            splitJoint = url + "?" + parameterString;
        } else {
            splitJoint = url;
        }

        return splitJoint;
    }

    private static RequestConfig.Builder customRequestConfigBuilder() {
        return RequestConfig.custom()
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                .setSocketTimeout(DEFAULT_CONNECT_TIMEOUT);
    }

    private static void setHeader(AbstractHttpMessage httpMessage, Map<String, String> requestHeader) {
        if (MapUtils.isNotEmpty(requestHeader)) {
            for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
                httpMessage.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    public static String executeHttpRequest(boolean isHttps, HttpRequestBase httpRequestBase) throws IOException {
        HttpClient httpClient;
        try {
            httpClient = isHttps ? buildCloseableHttpClient() : HttpUtil.HTTP_CLIENT;
        } catch (GeneralSecurityException e) {
            log.error("Fail to build CloseableHttpClient.", e);
            return null;
        }

        try {
            HttpResponse httpResponse = httpClient.execute(httpRequestBase);

            if (httpResponse == null || httpResponse.getEntity() == null) {
                log.error("Fail to execute http request or http response is null.");
                return null;
            }

            try (InputStream inputStream = httpResponse.getEntity().getContent()) {
                return IOUtils.toString(inputStream, DEFAULT_CHARSET);
            }
        } finally {
            httpRequestBase.releaseConnection();

            if (httpClient instanceof CloseableHttpClient) {
                try {
                    ((CloseableHttpClient) httpClient).close();
                } catch (IOException e) {
                    log.error("Fail to close CloseableHttpClient.", e);
                }
            }
        }
    }
}
