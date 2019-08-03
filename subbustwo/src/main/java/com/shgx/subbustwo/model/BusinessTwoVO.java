package com.shgx.subbustwo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * @author: guangxush
 * @create: 2019/08/03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessTwoVO {
    /**
     * 用户ID
     */
    @Column(name = "uid")
    private String uid;

    /**
     * 金额
     */
    @Column(name = "money")
    private Double money;


    /**
     * 交易状态
     */
    @Column(name = "status")
    private Integer status;
}
