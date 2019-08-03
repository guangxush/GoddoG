package com.shgx.subbus.subbusone.service;

import com.shgx.subbus.subbusone.model.BusinessOne;
import com.shgx.subbus.subbusone.model.BusinessOneVO;

/**
 * @author: guangxush
 * @create: 2019/08/03
 */
public interface BusinessOneService {
    /**
     * 查询交易
     *
     * @param uid
     * @return
     */
    BusinessOneVO queryBusinessOne(String uid);

    /**
     * 保存交易
     *
     * @param business
     * @return
     */
    Boolean saveBusinessOne(BusinessOne business);

    /**
     * 更新交易状态
     *
     * @param business
     * @return
     */
    Boolean updateBusinessOne(BusinessOne business);

}
