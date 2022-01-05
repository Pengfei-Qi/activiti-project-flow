package com.fei.activitiprojectflow.servicetask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * @description: 测试计时器
 * @author: qpf
 * @date: 2021/12/31
 * @version: 1.0
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class TimerTaskBean implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        String letter = (String) execution.getVariable("letter");

        System.out.println("测试定时器:  "+letter);
    }
}
