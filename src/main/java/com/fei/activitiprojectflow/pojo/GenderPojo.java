package com.fei.activitiprojectflow.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description:
 * @author: qpf
 * @date: 2021/12/29
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenderPojo implements Serializable {

    private String gender ;

    private String name = "凌凌漆";

    public String getGenderString(String gender){
        return "2. 执行GenderPojo. getGenderString方法  gender:"+gender;
    }
}
