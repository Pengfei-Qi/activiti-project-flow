package com.fei.activitiprojectflow;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @description: Activiti基本样例测试
 * @author: qpf
 * @date: 2021/12/28
 * @version: 1.0
 */
public class BussinessTest extends ActivitiProjectFlowApplicationTests {

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
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("myLeave1");
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
            System.out.println("当前流程定义ID:    " + processInstance.getDeploymentId());
        }
    }

    /**
     * 查询下一个任务节点信息
     */
    @Test
    public void findTaskTest() {
        String definitionKey = "myLeave1";
        List<Task> taskList = taskService.createTaskQuery().processDefinitionKey(definitionKey).orderByTaskCreateTime().asc().list();
        for (Task task : taskList) {
            System.out.println("===============================");
            System.out.println("下一个节点:   " + task.getAssignee());
            System.out.println("实例ID:   " + task.getProcessInstanceId());
            System.out.println("是否挂起:   " + task.isSuspended());
        }
    }

    /**
     * 完成任务
     */
    @Test
    public void completeTask() {
        String definitionKey = "myLeave1";
        String assingName = "wangwu";
        Task task = taskService.createTaskQuery().processDefinitionKey(definitionKey).taskAssignee(assingName).singleResult();
        if (task != null) {
            taskService.complete(task.getId());
            System.out.println(assingName + "    已完成");
        }
        List<Task> list = taskService.createTaskQuery().processDefinitionKey(definitionKey).orderByTaskCreateTime().desc().list();
        list.forEach((e) -> {
            System.out.println("下一个节点信息:    " + e.getAssignee());
            System.out.println("实例ID:  " + e.getProcessInstanceId());
        });
    }

    /**
     * 删除实例
     */
    @Test
    public void deleteInstance() {
        String instanceId = "d897a0e0-67e5-11ec-af92-00ff047fdcf5";
        runtimeService.deleteProcessInstance(instanceId, "删除多余的实例信息");
    }

    /**
     * 查询历史信息
     */
    @Test
    public void queryHistoryInfo() {
        String instanceId = "d897a0e0-67e5-11ec-af92-00ff047fdcf5";
        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId);
        //按照流程创建时间排序
        historicActivityInstanceQuery.orderByHistoricActivityInstanceStartTime().asc();
        List<HistoricActivityInstance> list = historicActivityInstanceQuery.list();
        list.forEach(e -> {
            System.out.println("活动ID: " + e.getActivityId());
            System.out.println("流程名称:  " + e.getActivityName());
            System.out.println("流程定义ID:  " + e.getProcessDefinitionId());
            System.out.println("实例ID:    " + e.getProcessInstanceId());
        });
    }

    /**
     * 删除流程定义,级联删除
     */
    @Test
    public void deleteDeployment() {
        String definitionKey = "myLeave1";
        List<Deployment> list = repositoryService.createDeploymentQuery().processDefinitionKey(definitionKey).list();
        list.forEach(deployment -> {
            System.out.println("流程定义id: " + deployment.getId());
            System.out.println("流程定义名称: " + deployment.getName());
            System.out.println("流程KEY:    " + deployment.getKey());
            //设置true 级联删除流程定义，即使该流程有流程实例启动也可以删除，设置为false非级联删除方式
            // repositoryService.deleteDeployment(deployment.getId());
            repositoryService.deleteDeployment(deployment.getId(), true);
            System.out.println("已删除完成.....");
        });


    }

    /**
     * 查询bpmn信息, 将bpmn文件下载到本地
     * @throws IOException
     */
    @Test
    public void queryBpmnFile() throws IOException {
        // 1、得到查询器：ProcessDefinitionQuery，设置查询条件,得到想要的流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myLeave1")
                .singleResult();
        // 2、通过流程定义信息，得到部署ID
        String deploymentId = processDefinition.getDeploymentId();
        // 3、通过repositoryService的方法，实现读取图片信息和bpmn信息
        //png图片的流
        // InputStream pngInput = repositoryService.getResourceAsStream(deploymentId, processDefinition.getDiagramResourceName());
        //bpmn文件的流
        InputStream bpmnInput = repositoryService.getResourceAsStream(deploymentId, processDefinition.getResourceName());
        //4、构造OutputStream流
        // File file_png = new File("d:/myLeave.png");
        File file_bpmn = new File("D:/myBussiness.bpmn20.xml");
        FileOutputStream bpmnOut = new FileOutputStream(file_bpmn);
        // FileOutputStream pngOut = new FileOutputStream(file_png);
        //5、输入流，输出流的转换
        // IOUtils.copy(pngInput,pngOut);
        IOUtils.copy(bpmnInput, bpmnOut);
        //5、关闭流
        // pngOut.close();
        bpmnOut.close();
        // pngInput.close();
        bpmnInput.close();
    }
}
