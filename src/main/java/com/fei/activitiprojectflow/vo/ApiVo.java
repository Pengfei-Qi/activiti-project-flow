package com.fei.activitiprojectflow.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @description: 基本对象
 * @author: qpf
 * @date: 2022/1/15
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApiVo {

    private int id;

    private String dueDate;

    private Object pram;
}
