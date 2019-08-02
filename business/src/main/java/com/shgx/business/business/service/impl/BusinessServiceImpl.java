package com.shgx.business.business.service.impl;

import com.shgx.business.business.model.Business;
import com.shgx.business.business.model.BusinessVO;
import com.shgx.business.business.repository.BusinessRepo;
import com.shgx.business.business.service.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Size;
import java.util.Date;
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

        try{
            score.setDate(new Date());
            save(score);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public Boolean updateBusiness(Business business) {
        return null;
    }

    private BusinessVO save(Business business){
        business = businessRepo.save(business);
        if(business.getId()<=0){
            log.error("fail to save the business:{}",business.toString());
        }
        return BusinessVO.builder()
                .uid(business.getUid())
                .money(business.getMoney())
                .status(business.getStatus())
                .build();
    }
}
