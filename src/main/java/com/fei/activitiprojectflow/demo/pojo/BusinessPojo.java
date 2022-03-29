package com.fei.activitiprojectflow.demo.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 出差单
 * @author: qpf
 * @date: 2021/12/28
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@ToString
public class BusinessPojo implements Serializable {

    /**
     * 主键Id
     */
    private Long id;


    /**
     * 出差单的名字
     */
    private String evectionName;
    /**
     * 出差天数
     */
    private Double num;

    /**
     * 开始时间
     */
    private Date beginDate;

    /**
     * 出差结束时间
     */
    private Date endDate;

    /**
     * 目的地
     */
    private String destination;

    /**
     * 出差原因
     */
    private String reson;
}
