package com.fei.activitiprojectflow.demo.servicetask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @description: task实现类
 * @author: qpf
 * @date: 2021/12/29
 * @version: 1.0
 */
public class ToUpperCaseFieldInjected implements JavaDelegate {

    private Expression pram1;
    private Expression pram2;
    @Autowired
    MyDelegateExpressionBean myDelegateExpressionBean;

    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("===========ToUpperCaseFieldInjected=============");
        //1.  执行 "${genderPojo.getGenderString(gender)}" 调用方法执行,
        String value1 = (String) pram1.getValue(execution);
        execution.setVariable("letter2", new StringBuffer(value1).reverse().toString());

        System.out.println("打印值value1: "+ value1);
        //2.  执行 "Hello ${gender == 'male' ? 'Mr.' : 'Mrs.'} ${name}"
        String value2 = (String) pram2.getValue(execution);
        execution.setVariable("letter3", new StringBuffer(value2).reverse().toString());
        System.out.println("打印值value2: "+ value2);
        System.out.println("===========ToUpperCaseFieldInjected=============");
    }


}
