package com.shgx.subbus.subbusone.service.impl;

import com.shgx.subbus.subbusone.model.BusinessOne;
import com.shgx.subbus.subbusone.model.BusinessOneVO;
import com.shgx.subbus.subbusone.repository.BusinessOneRepo;
import com.shgx.subbus.subbusone.service.BusinessOneService;
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
public class BusinessOneServiceImpl implements BusinessOneService {

    @Autowired
    private BusinessOneRepo businessOneRepo;


    @Override
    public BusinessOneVO queryBusinessOne(String uid) {
        Optional<BusinessOne> BusinessOne = businessOneRepo.findByUid(uid);
        if (BusinessOne.isPresent()) {
            return BusinessOneVO.builder()
                    .uid(BusinessOne.get().getUid())
                    .money(BusinessOne.get().getMoney())
                    .status(BusinessOne.get().getStatus())
                    .build();
        }
        return null;
    }

    @Override
    public Boolean saveBusinessOne(BusinessOne BusinessOne) {
        Optional<List<BusinessOne>> businessDB = businessOneRepo.findAllByUid(BusinessOne.getUid());
        if (businessDB.isPresent()) {
            return true;
        }
        try {
            BusinessOne.setDate(new Date());
            save(BusinessOne);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean updateBusinessOne(BusinessOne businessOne) {
        Optional<BusinessOne> businessOneDB = businessOneRepo.findByUid(businessOne.getUid());
        if (businessOneDB.isPresent() && businessOne != null) {
            BusinessOne business = businessOneDB.get();
            business.setDate(new Date());
            business.setMoney(businessOne.getMoney());
            business.setStatus(businessOne.getStatus());
            business.setUid(businessOne.getUid());
            save(business);
        } else {
            log.error("the {} is not in db!", businessOne.toString());
            return false;
        }
        return true;
    }

    private BusinessOneVO save(BusinessOne businessOne) {
        businessOne = businessOneRepo.save(businessOne);
        if (businessOne.getId() <= 0) {
            log.error("fail to save the BusinessOne:{}", businessOne.toString());
        }
        return BusinessOneVO.builder()
                .uid(businessOne.getUid())
                .money(businessOne.getMoney())
                .status(businessOne.getStatus())
                .build();
    }
}

