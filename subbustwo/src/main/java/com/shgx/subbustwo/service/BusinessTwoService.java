package com.shgx.subbustwo.service;

import com.shgx.subbustwo.model.BusinessTwo;
import com.shgx.subbustwo.model.BusinessTwoVO;

/**
 * @author: guangxush
 * @create: 2019/08/03
 */
public interface BusinessTwoService {
    /**
     * 查询交易
     *
     * @param uid
     * @return
     */
    BusinessTwoVO queryBusinessTwo(String uid);

    /**
     * 保存交易
     *
     * @param business
     * @return
     */
    Boolean saveBusinessTwo(BusinessTwo business);

    /**
     * 更新交易状态
     *
     * @param business
     * @return
     */
    Boolean updateBusinessTwo(BusinessTwo business);

}
