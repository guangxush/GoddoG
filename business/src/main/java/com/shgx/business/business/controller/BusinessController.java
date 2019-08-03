package com.shgx.business.business.controller;

import com.shgx.business.business.model.Business;
import com.shgx.business.business.model.BusinessVO;
import com.shgx.business.business.service.BusinessService;
import com.shgx.common.common.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: guangxush
 * @create: 2019/08/02
 */
@RestController
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @RequestMapping(path = "/query/{uid}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<BusinessVO> queryScore(@PathVariable("uid") String uid) {
        if (uid == null) {
            return new ApiResponse<BusinessVO>().fail(new BusinessVO());
        }
        BusinessVO businessVO = businessService.queryBusiness(uid);
        return new ApiResponse<BusinessVO>().success(businessVO);
    }


    @RequestMapping(path = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse<Boolean> insertBusiness(@RequestBody Business business) {
        Boolean result = businessService.saveBusiness(business);
        return new ApiResponse<Boolean>().success(result);
    }


    @RequestMapping(path = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public ApiResponse<Boolean> updateBusiness(@RequestBody Business business) {
        Boolean result = businessService.updateBusiness(business);
        return new ApiResponse<Boolean>().success(result);
    }
}
