package com.fei.activitiprojectflow.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

/**
 * @description: 执行监视器
 *                       start：开始时触发
 *                       end：结束时触发
 *                       take：主要用于监控流程线，当流程流转该线时触发
 * @author: qpf
 * @date: 2021/12/29
 * @version: 1.0
 */
public class ExecutionListenerImpl implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) {
        //暂时注释掉

        String event = execution.getEventName();
        switch (event) {
            case "start" :
                System.out.println("start event");
                break;
            case "end" :
                System.out.println("end event"+execution.getId());
                break;
            case "take" :
                System.out.println("take event");
                break;
        }

    }
}
