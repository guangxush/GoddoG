package com.shgx.subbustwo.service.impl;

import com.shgx.subbustwo.service.BusinessTwoService;
import com.shgx.subbustwo.model.BusinessTwo;
import com.shgx.subbustwo.model.BusinessTwoVO;
import com.shgx.subbustwo.reporsitory.BusinessTwoRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author: guangxush
 * @create: 2019/08/03
 */
@Service
@Slf4j
public class BusinessTwoServiceImpl implements BusinessTwoService {

    @Autowired
    private BusinessTwoRepo businessTwoRepo;


    @Override
    public BusinessTwoVO queryBusinessTwo(String uid) {
        Optional<BusinessTwo> BusinessOne = businessTwoRepo.findByUid(uid);
        if (BusinessOne.isPresent()) {
            return BusinessTwoVO.builder()
                    .uid(BusinessOne.get().getUid())
                    .money(BusinessOne.get().getMoney())
                    .status(BusinessOne.get().getStatus())
                    .build();
        }
        return null;
    }

    @Override
    public Boolean saveBusinessTwo(BusinessTwo BusinessTwo) {
        Optional<List<BusinessTwo>> businessDB = businessTwoRepo.findAllByUid(BusinessTwo.getUid());
        if(businessDB.isPresent()){
            return true;
        }
        try {
            BusinessTwo.setDate(new Date());
            save(BusinessTwo);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean updateBusinessTwo(BusinessTwo businessTwo) {
        Optional<BusinessTwo> businessTwoDB = businessTwoRepo.findByUid(businessTwo.getUid());
        if (businessTwoDB.isPresent() && businessTwo != null) {
            BusinessTwo business = businessTwoDB.get();
            business.setDate(new Date());
            business.setMoney(businessTwo.getMoney());
            business.setStatus(businessTwo.getStatus());
            business.setUid(businessTwo.getUid());
            save(business);
        } else {
            log.error("the {} is not in db!", businessTwo.toString());
            return false;
        }
        return true;
    }

    private BusinessTwoVO save(BusinessTwo businessTwo) {
        businessTwo = businessTwoRepo.save(businessTwo);
        if (businessTwo.getId() <= 0) {
            log.error("fail to save the BusinessTwo:{}", businessTwo.toString());
        }
        return BusinessTwoVO.builder()
                .uid(businessTwo.getUid())
                .money(businessTwo.getMoney())
                .status(businessTwo.getStatus())
                .build();
    }
}

