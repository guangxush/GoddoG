package com.shgx.business.business.service.impl;

import com.shgx.business.business.model.Business;
import com.shgx.business.business.model.BusinessVO;
import com.shgx.business.business.repository.BusinessRepo;
import com.shgx.business.business.service.BusinessCore;
import com.shgx.business.business.service.BusinessService;
import com.shgx.business.business.service.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author: guangxush
 * @create: 2019/08/02
 */
@Service
@Slf4j
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private BusinessRepo businessRepo;


//    @Autowired
//    private BusinessCore businessCore;


    @Autowired
    private RequestService requestService;

    // todo 可配置化
    private final String[] urls = {"http://localhost:8082/businesstwo/insert",
            "http://localhost:8081/businessone/insert"};


    @Override
    public BusinessVO queryBusiness(String uid) {
        Optional<Business> business = businessRepo.findByUid(uid);
        if (business.isPresent()) {
            return BusinessVO.builder()
                    .uid(business.get().getUid())
                    .money(business.get().getMoney())
                    .status(business.get().getStatus())
                    .build();
        }
        return null;
    }

    @Override
    public Boolean saveBusiness(Business business) {
        Optional<List<Business>> businessDB = businessRepo.findAllByUid(business.getUid());
        if (businessDB.isPresent()) {
            return true;
        }
        try {
            business.setDate(new Date());
            save(business);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean updateBusiness(Business business) {
        Optional<Business> businessDB = businessRepo.findByUid(business.getUid());
        if (businessDB.isPresent()) {
            businessDB.get().setDate(new Date());
            save(business);
        } else {
            log.error("the {} is not in db!", business.toString());
            return false;
        }
        return true;
    }

    private BusinessVO save(Business business) {
        BusinessVO businessVO = BusinessVO.builder()
                .uid(business.getUid())
                .money(business.getMoney())
                .build();
        if (requestService.doBusiness(urls, businessVO)) {
            business.setStatus(2);
            business = businessRepo.save(business);
            if (business.getId() <= 0) {
                log.error("fail to save the business:{}", business.toString());
                return null;
            }
        }
        return businessVO;
    }
}
