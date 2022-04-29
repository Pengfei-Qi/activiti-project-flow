package com.fei.activitiprojectflow.service;


import com.fei.activitiprojectflow.vo.ActivitiVo;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ActivitiServiceImpl implements ActivitiService {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;

    private final String processDefinitionKey = "ASK_FOR_LEAVE_ACT";


    @Override
    public ArrayList<Map> queryUserTaskByUserName(ActivitiVo activitiVo) {
        ArrayList<Map> list = new ArrayList<>();;
        taskService.createTaskQuery().taskAssignee(activitiVo.getUsername()).list().forEach((task)->{
            HashMap<String, Object> map = new HashMap<>();
            map.put("task",task);
            //根据任务ID获取当前流程实例
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            map.put("processInstance",historicProcessInstance);
            list.add(map);
        });
        return list;
    }

    @Override
    public ArrayList<Map> getHistoricProcessInstance(ActivitiVo activitiVo) {
        ArrayList<Map> list = new ArrayList<>();
        historyService.createHistoricTaskInstanceQuery().taskAssignee(activitiVo.getUsername()).list().forEach(historicTaskInstance -> {
            HashMap<String, Object> map = new HashMap<>();

            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(historicTaskInstance.getProcessInstanceId()).singleResult();
            map.put("processInstance",historicProcessInstance);
            //根据流程实例ID获取当前任务
            Task task = taskService.createTaskQuery().processInstanceId(historicTaskInstance.getProcessInstanceId()).taskAssignee(activitiVo.getUsername()).active().singleResult();
            map.put("task",task);

            list.add(map);
        });
        return list;
    }

    @Override
    public void completeUserTaskById(ActivitiVo activitiVo) {
        Task task = taskService.createTaskQuery().taskId(activitiVo.getTaskId()).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        Authentication.setAuthenticatedUserId(activitiVo.getUsername());

        taskService.claim(activitiVo.getTaskId(),activitiVo.getUsername());

        taskService.addComment(activitiVo.getTaskId(), processInstanceId, activitiVo.getMessage());

        Map<String, Object> variables = new HashMap<>();
        variables.put("auditFlag",activitiVo.isAuditFlag());
        taskService.complete(activitiVo.getTaskId(), variables);
    }

    /**
     * 获取所有的实例对象
     */
    @Override
    public List<Map<String, Object>> getAllInstanceInfo() {


        return runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey)
                .list().parallelStream().map(processInstance -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("processInstanceId", processInstance.getProcessInstanceId());
                    map.put("name", processInstance.getName());
                    map.put("businessKey", processInstance.getBusinessKey());
                    map.put("deploymentId", processInstance.getDeploymentId());
                    map.put("processDefinitionName", processInstance.getProcessDefinitionName());
                    return map;
                }).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getNextTaskInfo(String processInstanceId) {
        TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey).processInstanceId(processInstanceId);

        return taskQuery.list().parallelStream().map(task -> {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("processInstanceId",task.getProcessInstanceId());
            hashMap.put("assignee",task.getAssignee());
            hashMap.put("name",task.getName());
            hashMap.put("isSuspended",task.isSuspended());
            hashMap.put("createTime",task.getCreateTime());
            hashMap.put("id",task.getId());
            return hashMap;
        }).collect(Collectors.toList());
    }

}
