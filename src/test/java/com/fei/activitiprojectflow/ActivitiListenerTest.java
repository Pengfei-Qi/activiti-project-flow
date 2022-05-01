package com.fei.activitiprojectflow;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: qpf
 * @date: 2021/12/29
 * @version: 1.0
 */
public class ActivitiListenerTest extends ActivitiProjectFlowApplicationTests{
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

    final String processDefinitionKey1 =  "myListener" ;
    /**
     * 启动流程实例
     */
    @Test
    public void testStartProcess() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName","王晶晶");
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(processDefinitionKey1 ,variables);
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
    /**
     * 完成任务
     */
    @Test
    public void completeTask() {
        String assingName = "rose";
        Task task = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey1).taskAssignee(assingName).singleResult();
        if (task != null) {
            taskService.complete(task.getId());
            System.out.println(assingName + "    已完成");
        }
        List<Task> list = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey1).orderByTaskCreateTime().desc().list();
        list.forEach((e) -> {
            System.out.println("下一个节点信息:    " + e.getAssignee());
            System.out.println("实例ID:  " + e.getProcessInstanceId());
        });
    }

    /**
     * 查询历史信息
     */
    @Test
    public void queryHistoryInfo() {
        String instanceId = "620fd0ee-6862-11ec-a7eb-00ff047fdcf5";
        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId);
        //按照流程创建时间排序
        historicActivityInstanceQuery.orderByHistoricActivityInstanceStartTime().asc();
        List<HistoricActivityInstance> list = historicActivityInstanceQuery.list();
        list.forEach(e -> {
            System.out.println("=======================");
            System.out.println("活动ID: " + e.getActivityId());
            System.out.println("流程名称:  " + e.getActivityName());
            System.out.println("流程定义ID:  " + e.getProcessDefinitionId());
            System.out.println("实例ID:    " + e.getProcessInstanceId());
        });
    }
}
