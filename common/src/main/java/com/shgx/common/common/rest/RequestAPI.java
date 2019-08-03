package com.shgx.common.common.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.shgx.common.common.enums.ApiCodeEnum;
import com.shgx.common.common.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import com.google.common.cache.LoadingCache;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author: guangxush
 * @create: 2019/08/03
 */
@Slf4j
public class RequestAPI {
    private RestTemplateProvider provider = new RestTemplateProvider();

    private ObjectMapper mapper = new ObjectMapper();

    private String url;
    private Boolean useCache = true;
    private TimeUnit expireTimeUnit = TimeUnit.HOURS;
    private Integer expireDuration = 10;
    private Integer cacheSize = 1000;

    private int CONFIG_READ_TIMEOUT_IN_MILLI = 500;

    private LoadingCache<String, String> configCache;

    private final static String CONFIG_CACHE_PREFIX = "LEMON-CONFIG-";

    public void init() {

        // 设置超时时间
        SimpleClientHttpRequestFactory rf = new SimpleClientHttpRequestFactory();
        rf.setReadTimeout(CONFIG_READ_TIMEOUT_IN_MILLI);
        rf.setConnectTimeout(CONFIG_READ_TIMEOUT_IN_MILLI);
        provider.getRestTemplate().setRequestFactory(rf);

        if (useCache) {
            log.info("Init cache with param, duration={}, timeUnit={}", expireDuration, expireTimeUnit);
            configCache = CacheBuilder.newBuilder()
                    .recordStats()
                    .maximumSize(cacheSize)
                    .expireAfterAccess(expireDuration, expireTimeUnit)
                    .build(
                            new CacheLoader<String, String>() {
                                @Override
                                public String load(String key) throws Exception {
                                    String value = internalGet(key.replace(CONFIG_CACHE_PREFIX, ""));
                                    if (value != null) {
                                        return value;
                                    }
                                    return null;
                                }
                            }
                    );
        }
    }

    /**
     * 获取值，如取不到则返回默认值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String get(String key, String defaultValue) {
        try {
            String value = get(key);
            if (value == null) {
                return defaultValue;
            } else {
                return value;
            }
        } catch (InternalError e) {
            log.warn("fail to get config, return default value, key={}, defaultValue={}", key, defaultValue);
            return defaultValue;
        }
    }

    /**
     * 获取值
     *
     * @param key
     * @return
     * @throws InternalError
     */
    public String get(String key) throws InternalError {
        if (key == null) {
            log.error("the url param is null");
            throw new InternalError("the url param is null");
        }
        if (!useCache) {
            return internalGet(key);
        }
        String value = null;
        // 从缓存中获取
        try {
            value = configCache.get(CONFIG_CACHE_PREFIX + key);
        } catch (ExecutionException e) {
            log.error("take config from guava cache error, key : {}", key, e);
        } catch (InternalError e) {
            log.error("take config from guava cache error, key : {}", key, e);
        }
        return value;
    }

    /**
     * 无需设置缓存的config使用该方法获取
     *
     * @param key
     * @return
     * @throws InternalError
     */
    public boolean internalGet(String url, String key) throws InternalError {
        url = url + "/" + key;

        HttpHeaders requestHeaders = new HttpHeaders();
        //requestHeaders.add(GatewayHeadConstants.TYPE_HEAD, "config.client"); // if have head
        HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);
        HttpEntity<String> response = internalRequest(url, requestEntity, 3);
        try {
            ApiResponse apiResponse = mapper.readValue(response.getBody(), ApiResponse.class);
            if (ApiCodeEnum.SUCCESS.equals(apiResponse.getCode()) && apiResponse.getData() != null) {
                // 设置合适的类
                // Object config = mapper.convertValue(apiResponse.getData(), Object.class);
                Boolean result = mapper.convertValue(apiResponse.getData(), Boolean.class);
                return result;
            } else {
                throw new InternalError("fetch config error");
            }
        } catch (IOException e) {
            log.error("cast response body error", e);
            throw new InternalError("fetch config error");
        }
    }

    public String internalGet(String key) throws InternalError {
        url = this.url + "/" + key;

        HttpHeaders requestHeaders = new HttpHeaders();
        //requestHeaders.add(GatewayHeadConstants.TYPE_HEAD, "config.client"); // if have head
        HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);
        HttpEntity<String> response = internalRequest(url, requestEntity, 3);
        try {
            ApiResponse apiResponse = mapper.readValue(response.getBody(), ApiResponse.class);
            if (ApiCodeEnum.SUCCESS.equals(apiResponse.getCode()) && apiResponse.getData() != null) {
                // 设置合适的类
                Object config = mapper.convertValue(apiResponse.getData(), Object.class);
                return config.toString();
            } else {
                throw new InternalError("fetch config error");
            }
        } catch (IOException e) {
            log.error("cast response body error", e);
            throw new InternalError("fetch config error");
        }
    }

    public HttpEntity<String> internalRequest(String url, HttpEntity<String> requestEntity, int retryTimes) {
        try {
            return provider.getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, String.class);
        } catch (ResourceAccessException e) {
            if (retryTimes > 0) {
                retryTimes--;
                log.warn("Retry request, url={}, times={}", url, retryTimes);
                return internalRequest(url, requestEntity, retryTimes);
            } else {
                throw e;
            }
        }
    }
}
