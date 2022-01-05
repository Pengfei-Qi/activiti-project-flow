package com.fei.activitiprojectflow.servicetask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

/**
 * @description: task实现类
 * @author: qpf
 * @date: 2021/12/29
 * @version: 1.0
 */
@Component
public class MyDelegateExpressionBean implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("===========MyDelegateExpressionBean=============");
        // String var = (String) execution.getVariable("var");
        // System.out.println("从全局变量中取值var:    "+var);
        String letter1 = (String) execution.getVariable("letter");
        System.out.println("从全局变量中取值letter1:    "+letter1);
        String letter2 = (String) execution.getVariable("letter2");
        System.out.println("从全局变量中取值letter2:    "+letter2);
        String letter3 = (String) execution.getVariable("letter3");
        System.out.println("从全局变量中取值letter3:    "+letter3);
        System.out.println("===========MyDelegateExpressionBean=============");

    }
}
