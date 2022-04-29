package com.fei.activitiprojectflow.config;

import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
*
 * @description: 设置Activity 全局监听器
 * @author: qpf
 * @date: 2021/12/29
 * @version: 1.0
*/
@Component
public class ActivitiConfig implements ProcessEngineConfigurationConfigurer {

    @Autowired
    private ActivitiEventListener activitiEventListener;

    @Override
    public void configure(SpringProcessEngineConfiguration configuration) {
        Map<String, List<ActivitiEventListener>> activitiEventListenerMap = new HashMap<>();
        // 设置全局监听器
        activitiEventListenerMap.put(ActivitiEventType.ACTIVITY_STARTED.name(), Lists.newArrayList(activitiEventListener));
        activitiEventListenerMap.put(ActivitiEventType.ACTIVITY_COMPLETED.name(), Lists.newArrayList(activitiEventListener));
        activitiEventListenerMap.put(ActivitiEventType.TASK_CREATED.name(), Lists.newArrayList(activitiEventListener));
        activitiEventListenerMap.put(ActivitiEventType.TASK_COMPLETED.name(), Lists.newArrayList(activitiEventListener));
        configuration.setTypedEventListeners(activitiEventListenerMap);
    }
}
