package com.fei.activitiprojectflow;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @description: 测试定时器
 * @author: qpf
 * @date: 2021/12/31
 * @version: 1.0
 */
public class ActivitiTimerTest  extends ActivitiProjectFlowApplicationTests{

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;

    // final private String processDefinitionKey1 =  "API_test" ;
    final private String processDefinitionKey1 =  "timerApi007" ;
    // final private String processDefinitionKey1 =  "API_test02" ;

    @Autowired
    private ManagementService managementService;

    /**
     * 启动流程实例
     */
    @Test
    public void testStartProcess() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("dueDate","R10/PT1M");
        variables.put("pram",400);
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

    @Test
    public void findJob(){
        managementService.createJobQuery().list().forEach(e -> {
            System.out.println(e.getId());
        });

    }

    @Test
    public void deleteInstance(){
        Arrays.asList("0da7c531-79d0-11ec-89f5-00ff047fdcf5","18dc46a2-8a62-11ec-82ec-00ff047fdcf5","3fbb4b2e-8efa-11ec-9a57-00ff047fdcf5","cb645a7e-8f06-11ec-a09c-00ff047fdcf5","ed524449-8f06-11ec-8f28-00ff047fdcf5","3cd560ec-8f08-11ec-a389-00ff047fdcf5").forEach(e -> {
            runtimeService.deleteProcessInstance(e,"删除任务");
        });

    }
    @Test
    public void queryJobHistory(){
        List<String> instanceIdList = new ArrayList<>();

        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().processDefinitionKey(processDefinitionKey1).list();
        for (HistoricProcessInstance instance : list) {
            System.out.println(instance.getId());
            System.out.println(instance.getName());
            System.out.println(instance.getStartTime());
            System.out.println(instance.getProcessDefinitionId());
            instanceIdList.add(instance.getId());
            System.out.println(instance.getDeploymentId());
            System.out.println("--------------------------------");
        }
        managementService.createJobQuery().list().forEach(e ->{
            System.out.println(e.getId());
            System.out.println(e.getDuedate());
            System.out.println(e.getJobType());
            System.out.println(e.getRetries());
            System.out.println(e.getExceptionMessage());
            System.out.println("--------- job---------------");
        });
        managementService.createDeadLetterJobQuery().list().forEach(e ->{
            System.out.println(e.getId());
            System.out.println(e.getDuedate());
            System.out.println(e.getJobType());
            System.out.println(e.getRetries());
            System.out.println(e.getExceptionMessage());
            System.out.println("----------dead letter--------------");
        });



        instanceIdList.forEach( instanceId -> {
            HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId);
            query.orderByHistoricActivityInstanceStartTime().asc();
            System.out.println("-------------- 当前的instanceId = "+ instanceId+"-------------");
            query.list().forEach(e -> {
                System.out.println(e.getStartTime());
                System.out.println(e.getActivityName());
                System.out.println(e.getTaskId());
                System.out.println("-----history job------");
            });
            System.out.println("-------------- 当前的instanceId = "+ instanceId+"-------------");
        });
    }

    @Test
    public void testDate(){
        System.out.println(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
    }

    @Test
    public void deleteDeployment(){
        HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery().processInstanceId("009f20fb-79b6-11ec-8c78-00ff047fdcf5").singleResult();
        repositoryService.deleteDeployment(instance.getDeploymentId(),true);
    }

    


}
