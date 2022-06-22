package com.fei.activitiprojectflow.demo.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

/**
 * @description: 执行监视器
 *                       start：开始时触发
 *                       end：结束时触发
 *                       take：主要用于监控流程线，当流程流转该线时触发
 * @author: qpf
 * @date: 2021/12/29
 * @version: 1.0
 */
@Slf4j
public class ExecutionListenerImpl implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) {
        String event = execution.getEventName();
        switch (event) {
            case "start" :
                // System.out.println("start event");
                log.info("ExecutionListenerImpl-----------------start event");
                System.out.println("流程状态------start: 流程激活:"+execution.isActive());
                System.out.println("流程状态------start--------流程终止: "+execution.isEnded());
                break;
            case "end" :
                // System.out.println("end event"+execution.getId());
                log.info("ExecutionListenerImpl-----------------end event:{}",execution.getId());
                System.out.println("流程状态------end------status: "+execution.isActive());
                System.out.println("流程状态------end--------流程终止: "+execution.isEnded());
                System.out.println("流程状态------end------processDefinitionId: "+execution.getProcessDefinitionId());
                System.out.println("流程状态------end------instanceId: "+execution.getProcessInstanceId());
                break;
            case "take" :
                log.info("ExecutionListenerImpl-----------------take event");
                break;
        }

    }
}
