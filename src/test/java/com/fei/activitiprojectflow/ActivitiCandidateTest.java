package com.fei.activitiprojectflow;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @description: 测试 候选人
 * @author: qpf
 * @date: 2021/12/28
 * @version: 1.0
 */
public class ActivitiCandidateTest extends ActivitiProjectFlowApplicationTests{

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;

    final String processDefinitionKey1 = "myCandidate";

    /**
     * 启动流程实例
     */
    @Test
    public void testStartProcess() {
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(processDefinitionKey1);
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
        String assingName = "peter3";
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
        String instanceId = "ba7abb3b-67f0-11ec-a58c-00ff047fdcf5";
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

    /**
     * springSecurity 结合,候选人需为security 的用户
     */
    @Test
    public void queryCandidateTaskTest(){
        String candidateUser = "admin";
        List<Task> list = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey1).taskCandidateUser(candidateUser).list();
        list.forEach(task -> {
            System.out.println("========================");
            System.out.println("流程实例ID="+task.getProcessInstanceId());
            System.out.println("任务id="+task.getId());
            System.out.println("任务负责人="+task.getAssignee());
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
     * 候选人拾取任务, 归还任务
     */
    @Test
    public void candidateClaimTest(){
        String taskId = "f023744c-67f0-11ec-9d75-00ff047fdcf5";
        String candidateUser = "jack";
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task != null) {
            //拾取任务
            taskService.claim(taskId,candidateUser);
            //任务归还, 第一种写法
            // taskService.unclaim(taskId);
            //任务归还, 第二种写法
            // taskService.claim(taskId,null);
            System.out.println("任务名称:"+task.getName() + "---id: "+taskId + "拾取任务完成");
        }
    }

    /**
     * 任务指派: 当存在一组用户信息,可以指派给其他人
     */
    @Test
    public void candidateUnClaimTest(){
        String taskId = "f023744c-67f0-11ec-9d75-00ff047fdcf5";
        String assignUser = "jack";
        Task task = taskService.createTaskQuery().taskId(taskId).taskAssignee(assignUser).singleResult();
        if (task != null) {
            //任务指派
            taskService.setAssignee(taskId, "other");
            System.out.println("任务名称:"+task.getName() + "---id: "+taskId + "指派任务完成");
        }
    }

}
