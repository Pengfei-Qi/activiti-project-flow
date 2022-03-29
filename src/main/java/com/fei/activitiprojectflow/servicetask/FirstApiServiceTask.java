package com.fei.activitiprojectflow.servicetask;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * @description:  实现类
 * @author: qpf
 * @date: 2022/1/15
 * @version: 1.0
 */
@Component
@Slf4j
@Scope(SCOPE_PROTOTYPE)
public class FirstApiServiceTask implements JavaDelegate {

    @SneakyThrows
    @Override
    public void execute(DelegateExecution execution) {
        log.info(" firstApi ---->defineID:{} \t instanceId {} \t activity: {}",execution.getProcessDefinitionId(),execution.getProcessInstanceId(),execution.isActive());
        // String dueDate = String.valueOf(execution.getVariable("cycleTime"));
        // log.info(" firstApi ---->cycleTime:{} \t 当前时间: {}",dueDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        // String pram = String.valueOf(execution.getVariable("pram"));
        // log.info(" firstApi ---->pram:{}",pram);


        // TimeUnit.MINUTES.sleep(15);
        log.info("firstApi end process");
    }


}
