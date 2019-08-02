package com.shgx.business.business.service;

import com.shgx.business.business.model.Business;
import com.shgx.business.business.model.BusinessVO;

/**
 * @author: guangxush
 * @create: 2019/08/02
 */
public interface BusinessService {
    /**
     * 查询交易
     *
     * @param uid
     * @return
     */
    BusinessVO queryBusiness(String uid);

    /**
     * 保存交易
     *
     * @param business
     * @return
     */
    Boolean saveBusiness(Business business);

    /**
     * 更新交易状态
     *
     * @param business
     * @return
     */
    Boolean updateBusiness(Business business);
}
