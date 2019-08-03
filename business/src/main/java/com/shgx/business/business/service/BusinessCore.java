package com.shgx.business.business.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shgx.business.business.model.BusinessVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: guangxush
 * @create: 2019/08/01
 */
@Service
@Slf4j
public class BusinessCore {


    @Autowired
    private SendMessage sendMessage;

    public boolean doBusiness(BusinessVO businessVO){
        if(doTry(businessVO)){
            doConfirm(businessVO);
            return true;
        }else{
            doCancel(businessVO);
            return false;
        }
    }

    private boolean doTry(BusinessVO businessVO){
        try{
            businessVO.setStatus(1);
            String json = JSONObject.toJSONString(businessVO);
            log.info("send data:"+ json);
            boolean result = sendMessage.send(json);
            return result;
        }catch (Exception e){
            return false;
        }
    }

    private boolean doConfirm(BusinessVO businessVO){
        try{
            businessVO.setStatus(2);
            String json = JSONObject.toJSONString(businessVO);
            log.info("confirm data:"+ json);
            boolean result = sendMessage.send(json);
            return result;
        }catch (Exception e){
            return false;
        }
    }


    private boolean doCancel(BusinessVO businessVO){
        try{
            businessVO.setStatus(0);
            String json = JSONObject.toJSONString(businessVO);
            log.info("confirm data:"+ json);
            boolean result = sendMessage.send(json);
            return result;
        }catch (Exception e){
            return false;
        }
    }
}
