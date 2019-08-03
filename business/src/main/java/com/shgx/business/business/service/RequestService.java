package com.shgx.business.business.service;

import com.shgx.common.common.rest.RequestAPI;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author: guangxush
 * @create: 2019/08/03
 */
@Service
public class RequestService {

    private RequestAPI requestAPI = new RequestAPI();

    @PostConstruct
    public void init(){
        requestAPI.init();
    }

    public boolean doTry(String url, String uid){
        boolean result = requestAPI.internalGet(url, uid);
        return result;
    }
}
