package com.shgx.common.common.rest;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * @author: guangxush
 * @create: 2019/08/03
 */
public class RestTemplateProvider {
    private RestTemplate restTemplate;

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public RestTemplateProvider(){
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setReadTimeout(10000);
        httpComponentsClientHttpRequestFactory.setConnectTimeout(10000);
        restTemplate = new RestTemplate(httpComponentsClientHttpRequestFactory);
        restTemplate.getMessageConverters().clear();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }
}
