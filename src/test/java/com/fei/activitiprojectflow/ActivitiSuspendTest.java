package com.fei.activitiprojectflow;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @description: 测试挂起,激活,业务key
 * @author: qpf
 * @date: 2021/12/28
 * @version: 1.0
 */
public class ActivitiSuspendTest extends ActivitiProjectFlowApplicationTests{

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;

    /**
     * 启动流程实例,测试业务key
     */
    @Test
    public void businessKeyTest(){
        String businessKey = "2580";
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("myLeave1",businessKey);
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
        String key = "myLeave1";
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processDefinitionKey(key).list();
        for (ProcessInstance processInstance : list) {
            System.out.println("===========================");
            System.out.println("流程定义id:   " + processInstance.getProcessDefinitionId());
            System.out.println("流程实例id:    " + processInstance.getProcessInstanceId());
            System.out.println("流程是否完成:    " + processInstance.isEnded());
            System.out.println("流程是否挂起:     " + processInstance.isSuspended());
            System.out.println("当前活动标识:     " + processInstance.getActivityId());
            System.out.println("活动关键字:       " + processInstance.getBusinessKey());
            System.out.println("当前DeploymentId ID:    " + processInstance.getDeploymentId());
        }
    }

    /**
     * 删除实例
     */
    @Test
    public void deleteInstance() {
        String instanceId = "aaf3efaa-67d9-11ec-b95e-00ff047fdcf5";
        runtimeService.deleteProcessInstance(instanceId, "删除多余的实例信息");
    }

    /**
     * 所有的流程实例被挂起, 激活
     */
    @Test
    public void suspendAllInstanceTest(){
        String key = "myLeave1";
        //查询流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).singleResult();
        //查询流程id
        String definitionId = processDefinition.getId();
        //获取状态 true: 挂起  false: 激活
        boolean suspended = processDefinition.isSuspended();
        //挂起状态,修改为激活状态
        if (suspended){
            // 如果是挂起，可以执行激活的操作,参数1：流程定义id 参数2：是否激活，参数3：激活时间
            repositoryService.activateProcessDefinitionById(definitionId,true,null);
            System.out.println(processDefinition.getName() +"        id===  "+definitionId +"  已被激活");
        }else {
            repositoryService.suspendProcessDefinitionById(definitionId,true,null);
            System.out.println(processDefinition.getName() +"        id===  "+definitionId +"  已被挂起");
        }


    }


    /**
     * 完成任务
     */
    @Test
    public void completeTask() {
        String definitionKey = "myLeave1";
        String assingName = "tom";
        String instanceId = "cd07ab71-67c6-11ec-8d2c-00ff047fdcf5";
        Task task = taskService.createTaskQuery().processDefinitionKey(definitionKey).taskAssignee(assingName).processInstanceId(instanceId).singleResult();
        if (task != null) {
            taskService.complete(task.getId());
            System.out.println(assingName + "    已完成");
        }
        List<Task> list = taskService.createTaskQuery().processDefinitionKey(definitionKey).orderByTaskCreateTime().desc().list();
        list.forEach((e) -> {
            System.out.println("下一个节点信息:    " + e.getAssignee());
            System.out.println("实例ID:  " + e.getProcessInstanceId());
            System.out.println("流程定义id:  "+e.getProcessDefinitionId());
        });
    }

    /**
     * 指定实例被挂起, 激活
     */
    @Test
    public void suspendInstanceTest(){
        //查询所有实例信息
        String definitionKey = "myLeave1";
        String instanceId = "myLeave1:1:39687394-67b9-11ec-a627-00ff047fdcf5";
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processDefinitionKey(definitionKey).list();
        list.forEach(processInstance -> {
            System.out.println("===========================");
            boolean suspended = processInstance.isSuspended();
            String processInstanceId = processInstance.getProcessInstanceId();
            System.out.println("流程实例id:    " + processInstanceId);
            System.out.println("流程是否完成:    " + processInstance.isEnded());
            System.out.println("流程是否挂起:     " + suspended);
            System.out.println("活动关键字:       " + processInstance.getBusinessKey());
            //指定实例id进行激活\挂起操作
            if (instanceId.equalsIgnoreCase(processInstanceId)) {
                //已被挂起, 激活该实例
                if (suspended){
                    //挂起实例
                    runtimeService.activateProcessInstanceById(processInstanceId);
                    System.out.println(processInstanceId +"      名称:"+processInstance.getName() +" 已被激活");
                }else{
                    //激活实例对象
                    runtimeService.suspendProcessInstanceById(processInstanceId);
                    System.out.println(processInstanceId +"      名称:"+processInstance.getName() +" 已被挂起");
                }
            }
        });
    }

}
