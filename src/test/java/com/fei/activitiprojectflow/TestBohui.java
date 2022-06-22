package com.fei.activitiprojectflow;

import com.fei.activitiprojectflow.demo.pojo.BusinessPojo;
import com.fei.activitiprojectflow.jump.JumpAnyWhereCmd;
import org.activiti.engine.*;
import org.activiti.engine.impl.TaskServiceImpl;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description:  测试驳回流程
 * @author: qpf
 * @date: 2022/6/20
 * @version: 1.0
 */
public class TestBohui extends ActivitiProjectFlowApplicationTests {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;

    final private String processDefinitionKey1 =  "process_bohui" ;
    // final private String processDefinitionKey1 =  "test_process2" ;

    @Autowired
    private ManagementService managementService;

    @Test
    public void businessKeyTest(){
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(processDefinitionKey1);
        //输出内容
        System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id：" + processInstance.getId());
        System.out.println("当前活动Id：" + processInstance.getActivityId());
        System.out.println("businessKey:     "+processInstance.getBusinessKey());
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
        String assingName = "dev";
        Task task = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey1).taskAssignee(assingName).singleResult();
        if (task != null) {
            //修改变量
            taskService.complete(task.getId());
            System.out.println(assingName + "    已完成");
        }
        List<Task> list = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey1).orderByTaskCreateTime().desc().list();
        list.forEach((e) -> {
            System.out.println("================================");
            System.out.println("任务人:    " + e.getAssignee());
            System.out.println("实例ID:  " + e.getProcessInstanceId());
            System.out.println("taskId:     "+e.getId());
            System.out.println("任务名称:  " + e.getName());
            System.out.println("任务创建时间:  " + e.getCreateTime());
            System.out.println("任务所属流程定义ID:  " + e.getProcessDefinitionId());
            System.out.println("任务执行id:     "+e.getExecutionId());
            System.out.println("父任务id:    "+e.getParentTaskId());
            System.out.println("task节点id:    "+e.getTaskDefinitionKey());
        });
    }

    /**
     * 删除多余的实例信息
     */
    @Test
    public void deleteInstanceTest(){
        runtimeService.deleteProcessInstance("f3e5a26f-67ef-11ec-8d4c-00ff047fdcf5","删除多的instance");
    }

    /**
     * 查询历史信息
     */
    @Test
    public void queryHistoryInfo() {
        String processIns = "ff6b62ea-f06a-11ec-8428-00ff047fdcf5";
        // historyService.createHistoricTaskInstanceQuery().processInstanceId(processIns).list().forEach(historicTaskInstance -> {
        //     System.out.println("[历史任务]任务id：" + historicTaskInstance.getId());
        //     System.out.println("[历史任务]任务名称：" + historicTaskInstance.getName());
        //     System.out.println("[历史任务]任务的责任人为: "+historicTaskInstance.getAssignee());
        //     System.out.println("[历史任务]taskDefId: "+historicTaskInstance.getTaskDefinitionKey());
        //     System.out.println("[历史任务]任务的状态为: "+historicTaskInstance.getDeleteReason());
        //     System.out.println("[历史任务]结束时间: "+historicTaskInstance.getEndTime());
        //
        //     // taskService.getTaskComments(e.getId()).forEach(comment -> {
        //     // 	System.out.println("备注信息: "+comment);
        //     // });
        //     System.out.println("-----------------------查询历史任务-----------------");
        // });
        historyService.createHistoricActivityInstanceQuery().processInstanceId(processIns).orderByHistoricActivityInstanceStartTime().asc().list().forEach(historicActivityInstance -> {
            System.out.println("[历史活动]活动id：" + historicActivityInstance.getId());
            System.out.println("[历史活动]活动名称：" + historicActivityInstance.getActivityName());
            System.out.println("[历史活动]活动的Id: "+historicActivityInstance.getActivityId());
            System.out.println("[历史活动]活动的类型为: "+historicActivityInstance.getActivityType());
            System.out.println("[历史活动]结束时间: "+historicActivityInstance.getEndTime());
            System.out.println("[历史活动]责任人: "+historicActivityInstance.getAssignee());
            System.out.println("[历史活动]删除理由: "+historicActivityInstance.getDeleteReason());
            System.out.println("-----------------------查询历史活动-----------------");
        }) ;
    }

    @Test
    public void backToFirstNode() {
        final String currentUser = "105";
        String targetNodeId = "sid-4E94CEC0-F069-11EC-B04E-00FF047FDCF5";
        String taskId = "dfe87e00-f06d-11ec-b95b-00ff047fdcf5";
        Map<String, Object> variables = taskService.getVariables(taskId);

        CommandExecutor commandExecutor = ((TaskServiceImpl) taskService).getCommandExecutor();
        commandExecutor.execute(new JumpAnyWhereCmd(taskId, targetNodeId, currentUser, variables, "回退指上次环节"));
    }


    @Test
    public void taskServiceHistory() {
        String processInstanceId = "ff6b62ea-f06a-11ec-8428-00ff047fdcf5";
        TaskQuery taskQuery = taskService.createTaskQuery();
        if (StringUtils.isNotEmpty(processInstanceId)) taskQuery.processInstanceId(processInstanceId);
        List<Task> list = taskQuery.orderByTaskCreateTime().desc().list();
        list.forEach((e) -> {
            System.out.println("================================");
            System.out.println("任务人:    " + e.getAssignee());
            System.out.println("实例ID:  " + e.getProcessInstanceId());
            System.out.println("taskId:     "+e.getId());
            System.out.println("任务名称:  " + e.getName());
            System.out.println("任务创建时间:  " + e.getCreateTime());
            System.out.println("任务所属流程定义ID:  " + e.getProcessDefinitionId());
            System.out.println("任务执行id:     "+e.getExecutionId());
            System.out.println("父任务id:    "+e.getParentTaskId());
            System.out.println("task节点id:    "+e.getTaskDefinitionKey());
        });
    }
}
