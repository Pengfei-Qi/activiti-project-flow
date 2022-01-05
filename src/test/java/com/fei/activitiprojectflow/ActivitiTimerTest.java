package com.fei.activitiprojectflow;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 测试定时器
 * @author: qpf
 * @date: 2021/12/31
 * @version: 1.0
 */
public class ActivitiTimerTest  {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;

    final String processDefinitionKey1 =  "myTimeService" ;

    /**
     * 启动流程实例
     */
    @Test
    public void testStartProcess() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("letter","abcdefg");
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(processDefinitionKey1,variables);
        //输出内容
        System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id：" + processInstance.getId());
        System.out.println("当前活动Id：" + processInstance.getActivityId());
    }

    /**
     * 查询流程实例相关信息
     */
    @Test
    public void findInstance() {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey1).list();
        for (ProcessInstance processInstance : list) {
            System.out.println("===========================");
            System.out.println("流程定义id:   " + processInstance.getProcessDefinitionId());
            System.out.println("流程实例id:    " + processInstance.getProcessInstanceId());
            System.out.println("流程是否完成:    " + processInstance.isEnded());
            System.out.println("流程是否挂起:     " + processInstance.isSuspended());
            System.out.println("当前活动标识:     " + processInstance.getActivityId());
            System.out.println("活动关键字:       " + processInstance.getBusinessKey());
            System.out.println("当前流程定义ID:    " + processInstance.getDeploymentId());
        }
    }

}
