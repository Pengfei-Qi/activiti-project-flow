package com.fei.activitiprojectflow.demo.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.springframework.stereotype.Component;

/**
 * @description: 事件监听器
 * @author: qpf
 * @date: 2021/12/29
 * @version: 1.0
 */

@Component
@Slf4j
public class ActivitiEventListenerImpl implements ActivitiEventListener {

    @Override
    public void onEvent(ActivitiEvent event) {
        // 这里可以根据需要自行强转event的实现类，如获取流转实例对象可强转为ActivitiEntityEventImpl，如获取环节信息可强转为ActivitiActivityEventImpl，等等等

        //暂时注释掉

        switch (event.getType()) {
            // case TASK_CREATED:
            //     System.out.println("TASK_CREATED event");
            //     log.info("事件名称: {} \t 事件类型 {}",event.getType().name(),event.getType());
            //     break;
            // case TASK_COMPLETED:
            //     // log.info("TASK_COMPLETED event");
            //     if (event.getType().name().startsWith("timer_")){
            //         log.info("{} 已执行完成",event.getType().name());
            //     }
            //     break;
            // case ACTIVITY_COMPLETED:
            //     System.out.println("ACTIVITY_COMPLETED event");
            //     break;
            // case ACTIVITY_STARTED:
            //     System.out.println("ACTIVITY_STARTED event");
            //     break;
            case PROCESS_COMPLETED:
                System.out.println("PROCESS_COMPLETED event");
                break;
            case PROCESS_STARTED:
                System.out.println("PROCESS_STARTED event");
                System.out.println("instanceId: "+event.getProcessInstanceId());
                System.out.println("processDefinitionId: "+event.getProcessDefinitionId());
                break;
        }
        // ActivitiEventType eventType = event.getType();
        // String name = eventType.name();
        //
        // if (name.startsWith("TIMER") || name.startsWith("JOB")){
        //     log.info("监听到job事件 {} \t {}",eventType,event.getProcessInstanceId());
        // }


    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
