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

import java.util.List;

/**
 * @description:  测试网关:  排他网关
 * @author: qpf
 * @date: 2021/12/28
 * @version: 1.0
 */
public class ActivitiExclusiveGatewayTest extends ActivitiProjectFlowApplicationTests{


    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;

   final String processDefinitionKey1 =  "myExclusive" ;

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
        String assingName = "financer";
        Task task = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey1).taskAssignee(assingName).singleResult();
        if (task != null) {
            taskService.complete(task.getId());
            System.out.println(assingName + "    已完成");
        }
        List<Task> list = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey1).orderByTaskCreateTime().desc().list();
        list.forEach((e) -> {
            System.out.println("下一个节点信息:    " + e.getAssignee());
            System.out.println("实例ID:  " + e.getProcessInstanceId());
            System.out.println("taskId           "+e.getId());
            BusinessPojo exclusive = (BusinessPojo) taskService.getVariable(e.getId(), "exclusive");
            System.out.println("查看变量数据:           "+exclusive.getNum());
        });
    }

    /**
     * 删除实例对象
     */
    @Test
    public void deleteInstance(){
        String processInstanceId = "fcffb7e1-67ed-11ec-ac14-00ff047fdcf5";
        runtimeService.deleteProcessInstance(processInstanceId,"删除多余的实例");
    }

    /**
     * 设置变量
     */
    @Test
    public void setVariable(){
        BusinessPojo businessPojo = new BusinessPojo();
        businessPojo.setNum(2d);
        String taskId = "8240b0e6-67e2-11ec-a3cd-00ff047fdcf5";
        taskService.setVariable(taskId,"exclusive",businessPojo);
    }
}
