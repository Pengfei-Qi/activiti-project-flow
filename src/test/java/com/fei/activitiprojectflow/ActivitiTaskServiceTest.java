package com.fei.activitiprojectflow;

import com.fei.activitiprojectflow.demo.pojo.GenderPojo;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 测试 serviceTask
 * @author: qpf
 * @date: 2021/12/29
 * @version: 1.0
 */
public class ActivitiTaskServiceTest extends ActivitiProjectFlowApplicationTests{

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;

    final String processDefinitionKey1 =  "myJavaService" ;

    /**
     * 启动流程实例
     */
    @Test
    public void testStartProcess() {
        Map<String, Object> variables = new HashMap<>();
        GenderPojo genderPojo = new GenderPojo();

        variables.put("genderPojo",genderPojo);
        variables.put("gender","sis");
        variables.put("name","张三");
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

    /**
     * 查询历史信息
     */
    @Test
    public void queryHistoryInfo() {
        String instanceId = "871ea190-6895-11ec-a7da-00ff047fdcf5";
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery().processDefinitionKey(processDefinitionKey1);
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


}
