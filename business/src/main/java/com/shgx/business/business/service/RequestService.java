package com.shgx.business.business.service;

import com.shgx.business.business.model.BusinessVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: guangxush
 * @create: 2019/08/03
 */
@Service
public class RequestService {

    @Autowired
    private PostRequestService postRequestService;

    @Transactional(rollbackFor = Exception.class)
    public boolean doBusiness(String[] urls, BusinessVO businessVO) {
        List<String> sendUrl = new ArrayList<>();
        for (String url : urls) {
            if (doTry(url, businessVO)) {
                sendUrl.add(url);
            }
        }
        if (sendUrl.size() == urls.length) {
            for (String url : sendUrl) {
                doConfirm(url, businessVO);
            }
            return true;
        } else {
            for (String url : sendUrl) {
                doCancel(url, businessVO);
            }
            return false;
        }
    }

    public boolean doTry(String url, BusinessVO businessVO) {
        businessVO.setStatus(1);
        return postRequestService.jsonRequest(url, businessVO);
    }

    public boolean doConfirm(String url, BusinessVO businessVO) {
        url = url.replace("insert", "update");
        businessVO.setStatus(2);
        return postRequestService.jsonRequest(url, businessVO);
    }

    public boolean doCancel(String url, BusinessVO businessVO) {
        businessVO.setStatus(3);
        return postRequestService.jsonRequest(url, businessVO);
    }
}
