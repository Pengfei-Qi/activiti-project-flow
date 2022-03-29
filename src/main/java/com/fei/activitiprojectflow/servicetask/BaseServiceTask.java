package com.fei.activitiprojectflow.servicetask;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * @description: 基本数据处理
 * @author: qpf
 * @date: 2022/1/15
 * @version: 1.0
 */
@Component
@Slf4j
@Scope(SCOPE_PROTOTYPE)
public class BaseServiceTask implements JavaDelegate {


    @Override
    public void execute(DelegateExecution execution) {
        log.info(" baseApi ---->eventName:{} \t instanceId {} \t activity: {}",execution.getEventName(),execution.getProcessInstanceId(),execution.isActive());
        String dueDate = String.valueOf(execution.getVariable("dueDate"));
        log.info(" baseApi ---->dueDate:{}\t 当前时间: {}",dueDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        Integer pram = (Integer) execution.getVariable("pram");
        log.info(" baseApi ---->pram:{}",pram);

        pram += 10;

        String s = execution.getCurrentFlowElement().toString();
        log.info("查询流程定义: ");
        log.info(" baseApi ---->pram2:{}",pram);
        execution.setVariable("pram",pram);
    }
}
