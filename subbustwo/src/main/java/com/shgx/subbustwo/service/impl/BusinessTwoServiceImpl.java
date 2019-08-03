package com.shgx.subbustwo.service.impl;

import com.shgx.subbustwo.service.BusinessTwoService;
import com.shgx.subbustwo.model.BusinessTwo;
import com.shgx.subbustwo.model.BusinessTwoVO;
import com.shgx.subbustwo.reporsitory.BusinessTwoRepo;
import lombok.extern.slf4j.Slf4j;
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
    public Boolean updateBusinessTwo(BusinessTwo BusinessTwo) {
        Optional<BusinessTwo> BusinessOneDB = businessTwoRepo.findByUid(BusinessTwo.getUid());
        if (BusinessOneDB.isPresent()) {
            BusinessTwo.setDate(new Date());
            save(BusinessTwo);
        } else {
            log.error("the {} is not in db!", BusinessTwo.toString());
            return false;
        }
        return true;
    }

    private BusinessTwoVO save(BusinessTwo BusinessTwo) {
        BusinessTwo = businessTwoRepo.save(BusinessTwo);
        if (BusinessTwo.getId() <= 0) {
            log.error("fail to save the BusinessTwo:{}", BusinessTwo.toString());
        }
        return BusinessTwoVO.builder()
                .uid(BusinessTwo.getUid())
                .money(BusinessTwo.getMoney())
                .status(BusinessTwo.getStatus())
                .build();
    }
}

