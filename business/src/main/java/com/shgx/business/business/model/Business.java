package com.shgx.business.business.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: guangxush
 * @create: 2019/08/02
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "business")
public class Business {
    /**
     * 自增id
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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


    /**
     * 交易时间
     */
    @Column(name = "datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
}
