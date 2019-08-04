package com.shgx.subbus.subbusone.controller;

import com.shgx.common.common.model.ApiResponse;
import com.shgx.subbus.subbusone.model.BusinessOne;
import com.shgx.subbus.subbusone.model.BusinessOneVO;
import com.shgx.subbus.subbusone.service.BusinessOneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: guangxush
 * @create: 2019/08/03
 */
@RestController
public class BusinessOneController {
    @Autowired
    private BusinessOneService businessService;

    @RequestMapping(path = "/query/{uid}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<BusinessOneVO> queryScore(@PathVariable("uid") String uid) {
        if (uid == null) {
            return new ApiResponse<BusinessOneVO>().fail(new BusinessOneVO());
        }
        BusinessOneVO businessVO = businessService.queryBusinessOne(uid);
        return new ApiResponse<BusinessOneVO>().success(businessVO);
    }


    @RequestMapping(path = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse<Boolean> insertBusiness(@RequestBody BusinessOne business) {
        Boolean result = businessService.saveBusinessOne(business);
        return new ApiResponse<Boolean>().success(result);
    }


    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse<Boolean> updateBusiness(@RequestBody BusinessOne business) {
        Boolean result = businessService.updateBusinessOne(business);
        return new ApiResponse<Boolean>().success(result);
    }
}
