package com.fei.activitiprojectflow.demo.servicetask;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TimerTaskBean implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        String letter = (String) execution.getVariable("letter");

        log.info(" TimerTaskBean ---->deploymentId:{} \t instanceId {} \t activity: {}",execution.getProcessDefinitionId(),execution.getProcessInstanceId(),execution.isActive());

        System.out.println("测试定时器:  "+letter);
    }
}
