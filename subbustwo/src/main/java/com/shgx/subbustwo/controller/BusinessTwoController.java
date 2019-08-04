package com.shgx.subbustwo.controller;

import com.shgx.common.common.model.ApiResponse;
import com.shgx.subbustwo.model.BusinessTwo;
import com.shgx.subbustwo.model.BusinessTwoVO;
import com.shgx.subbustwo.service.BusinessTwoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: guangxush
 * @create: 2019/08/03
 */
@RestController
public class BusinessTwoController {
    @Autowired
    private BusinessTwoService businessService;

    @RequestMapping(path = "/query/{uid}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<BusinessTwoVO> queryScore(@PathVariable("uid") String uid) {
        if (uid == null) {
            return new ApiResponse<BusinessTwoVO>().fail(new BusinessTwoVO());
        }
        BusinessTwoVO businessVO = businessService.queryBusinessTwo(uid);
        return new ApiResponse<BusinessTwoVO>().success(businessVO);
    }


    @RequestMapping(path = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse<Boolean> insertBusiness(@RequestBody BusinessTwo business) {
        Boolean result = businessService.saveBusinessTwo(business);
        return new ApiResponse<Boolean>().success(result);
    }


    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse<Boolean> updateBusiness(@RequestBody BusinessTwo business) {
        Boolean result = businessService.updateBusinessTwo(business);
        return new ApiResponse<Boolean>().success(result);
    }
}
