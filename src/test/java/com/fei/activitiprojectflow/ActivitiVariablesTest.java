package com.fei.activitiprojectflow;

import com.fei.activitiprojectflow.pojo.BusinessPojo;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:  测试变量
 * @author: qpf
 * @date: 2021/12/28
 * @version: 1.0
 */
public class ActivitiVariablesTest extends ActivitiProjectFlowApplicationTests{
    //
//    @Autowired
//    private ProcessRuntime processRuntime;
//
//    @Autowired
//    private TaskRuntime taskRuntime;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;

    /**
     * 启动流程实例
     */
    @Test
    public void testStartProcess() {
        Map<String, Object> variables = new HashMap<>();

        variables.put("assignee1","赵子龙2");
        variables.put("assignee2","孙权2");
        variables.put("assignee3","刘备2");
        variables.put("assignee4","张飞2");
        variables.put("assignee5","关羽2");

        BusinessPojo businessPojo = new BusinessPojo();
        businessPojo.setNum(1d);
        variables.put("businesspojo",businessPojo);

        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("myLeave2",variables);
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
        String key = "myLeave2";
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processDefinitionKey(key).list();
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
        String definitionKey = "myLeave2";
        String assingName = "关羽2";
        String instanceId = "b70fc259-67d9-11ec-a21a-00ff047fdcf5";
        Task task = taskService.createTaskQuery().processDefinitionKey(definitionKey).taskAssignee(assingName).processInstanceId(instanceId).singleResult();
        if (task != null) {
            //修改变量
            taskService.complete(task.getId());
            System.out.println(assingName + "    已完成");
        }
        List<Task> list = taskService.createTaskQuery().processDefinitionKey(definitionKey).orderByTaskCreateTime().desc().list();
        list.forEach((e) -> {
            BusinessPojo businesspojo = (BusinessPojo) taskService.getVariable(e.getId(), "businesspojo");
            System.out.println("================================");
            System.out.println("下一个节点信息:    " + e.getAssignee());
            System.out.println("实例ID:  " + e.getProcessInstanceId());
            System.out.println("taskId:     "+e.getId());
            System.out.println("assignee3   "+taskService.getVariable(e.getId(), "assignee3"));
            System.out.println("num:    "+ businesspojo.getNum());
        });
    }

    /**
     * 修改变量
     */
    @Test
    public void modifyVariable(){
        String taskId = "b7195f59-67d9-11ec-a21a-00ff047fdcf5";
        taskService.setVariable(taskId,"assignee3","王五3333");
        System.out.println("assignee3         "+taskService.getVariable(taskId, "assignee3"));
    }

    /**
     * 测试全局变量
     */
    @Test
    public void globalVarTest() {
        String definitionKey = "myLeave2";
        String taskId = "aafd8caa-67d9-11ec-b95e-00ff047fdcf5";
        BusinessPojo businessPojo = new BusinessPojo();
        businessPojo.setNum(5d);
        // taskService.setVariable(taskId,"businesspojo",businessPojo);

        taskService.setVariableLocal(taskId,"assignee3","刘备");


    }
}
