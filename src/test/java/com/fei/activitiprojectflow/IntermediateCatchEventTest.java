package com.fei.activitiprojectflow;

import com.fei.activitiprojectflow.demo.pojo.GenderPojo;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @description:
 * @author: qpf
 * @date: 2022/2/16
 * @version: 1.0
 */
public class IntermediateCatchEventTest extends ActivitiProjectFlowApplicationTests{

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;

    @Autowired
    private ManagementService managementService;

    final String processDefinitionKey1 =  "intermediateCatchEventTest" ;

    /**
     * 启动流程实例
     */
    @Test
    public void testStartProcess() {
        Map<String, Object> variables = new HashMap<>();
        GenderPojo genderPojo = new GenderPojo();

        variables.put("dueDate",new Date());
        variables.put("gender","sis");
        variables.put("name","张三");
        variables.put("letter","abcdefg");
        variables.put("timerVar", "PT10M");
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
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
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
     * 查询历史信息
     */
    @Test
    public void queryHistoryInfo() {
        String instanceId = "871ea190-6895-11ec-a7da-00ff047fdcf5";
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery().processInstanceId(instanceId);
        historicProcessInstanceQuery.list().forEach(e ->{
            System.out.println("流程id: "+e.getDeploymentId());
            System.out.println("流程名称"+e.getName());
            System.out.println("流程定义名称:  "+e.getProcessDefinitionName());
            System.out.println("开始事件:  "+e.getStartTime());
            System.out.println("结束时间:    "+e.getEndTime());
            System.out.println("==================================");
        });

        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery().processDefinitionKey(processDefinitionKey1);
        historicTaskInstanceQuery.list().forEach(e->{
            System.out.println("实例id:   "+e.getProcessInstanceId());
            System.out.println("任务名称:  "+e.getName());

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

    /*
    删除实例
     */
    @Test
    public void deleteInstance(){
        Arrays.asList("f146062f-9f79-11ec-bea5-00ff047fdcf5").forEach(e -> {
            runtimeService.deleteProcessInstance(e,"删除任务");
        });

    }
}
