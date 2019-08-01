package com.shgx.business.business.service.impl;

import com.shgx.business.business.model.Business;
import com.shgx.business.business.repository.BusinessRepo;
import com.shgx.business.business.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: guangxush
 * @create: 2019/08/02
 */
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private BusinessRepo businessRepo;


    @Override
    public Business queryBusiness(String uid) {
        return null;
    }

    @Override
    public Boolean saveBusiness(Business business) {
        return null;
    }

    @Override
    public Boolean updateBusiness(Business business) {
        return null;
    }
}
