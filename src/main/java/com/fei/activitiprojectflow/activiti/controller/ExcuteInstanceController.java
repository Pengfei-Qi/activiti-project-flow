package com.fei.activitiprojectflow.activiti.controller;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.history.HistoryLevel;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.activiti.engine.impl.persistence.entity.JobEntityImpl;
import org.activiti.engine.impl.test.JobTestHelper;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.JobQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @description: 流程实例执行
 * @author: qpf
 * @date: 2022/1/14
 * @version: 1.0
 */
@RestController
@RequestMapping("/instance")
@Slf4j
public class ExcuteInstanceController {

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

    @Autowired
    ProcessEngineConfiguration processEngineConfiguration;

    @RequestMapping("/queryAllInstance")
    public Map<String,Object> queryAllInstance(){
        Map<String,Object> map = new HashMap<>();
        List<Map<String,Object>> jobList  = new ArrayList<>();
        List<Map<String, Object>> list = runtimeService.createProcessInstanceQuery().list().parallelStream().map(processInstance -> (ExecutionEntityImpl) processInstance).map(e -> (Map<String, Object>) e.getPersistentState()).collect(Collectors.toList());
        list.forEach(e -> {
            Map<String,Object> jobQueryMap = new HashMap<>();
            String instanceId = (String) e.get("rootProcessInstanceId");
            JobQuery jobQuery = managementService.createJobQuery().processInstanceId(instanceId);
            jobQuery.orderByJobId().asc();
            List<Object> list1 = jobQuery.list().parallelStream().map(job -> (JobEntityImpl) job).map(jobEntity -> jobEntity.getPersistentState()).collect(Collectors.toList());
            jobQueryMap.put("instanceId",instanceId);
            jobQueryMap.put("list",list1);
            jobList.add(jobQueryMap);
        });
        map.put("processInstanceList",list);
        map.put("jobList",jobList);
        return map;
    }

    @SuppressWarnings("all")
    @RequestMapping("/excuteInstanceByKey")
    public Map<String,Object> excuteInstanceByKey(@RequestParam("key") String key,@RequestBody Map variables ){
        Map<String,Object> map = new HashMap<>(16);
        ProcessInstance processInstance = null;
        if (key.equalsIgnoreCase("repeatWithEnd")) {
            processInstance = validateTimerRepeat(key);
        }
        if (key.equalsIgnoreCase("boundaryRepeatWithEnd")) {
            processInstance = validateBoundaryTimer(key);
        }
        if (key.equalsIgnoreCase("boundaryB")) {
            Calendar calendar = Calendar.getInstance();
            Date baseTime = calendar.getTime();

            calendar.add(Calendar.MINUTE, 20);
            // expect to stop boundary jobs after 20 minutes
            DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
            DateTime dt = new DateTime(calendar.getTime());
            String dateStr = fmt.print(dt);

            // reset the timer
            // Calendar nextTimeCal = Calendar.getInstance();
            // nextTimeCal.setTime(baseTime);
            // processEngineConfiguration.getClock().setCurrentTime(nextTimeCal.getTime());
            processInstance = runtimeService.startProcessInstanceByKey("boundaryRepeatWithEnd");
            log.info("InstanceId(): {} \n dateStr:{}",processInstance.getId(),dateStr);
            runtimeService.setVariable(processInstance.getId(), "EndDateForBoundary", dateStr);
        }
        map.put("deploymentId",processInstance.getDeploymentId());
        map.put("processDefinitionName",processInstance.getProcessDefinitionName());
        map.put("instanceId",processInstance.getId());
        return map;
    }

    /**
     * key : repeatWithEnd
     */
    public ProcessInstance validateTimerRepeat(String key){
        Calendar calendar = Calendar.getInstance();
        Date baseTime = calendar.getTime();

        // expect to stop boundary jobs after 20 minutes
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();

        calendar.setTime(baseTime);
        calendar.add(Calendar.MINUTE, 32);
        // after 10 minutes the end Date will be reached but the intermediate timers will ignore it
        // since the end date is validated only when a new timer is going to be created

        DateTime dt = new DateTime(calendar.getTime());
        String dateStr1 = fmt.print(dt);

        calendar.setTime(baseTime);
        calendar.add(Calendar.HOUR, 1);
        // calendar.add(Calendar.MINUTE, 50);

        dt = new DateTime(calendar.getTime());
        String dateStr2 = fmt.print(dt);

        // reset the timer
        Calendar nextTimeCal = Calendar.getInstance();
        nextTimeCal.setTime(baseTime);
        processEngineConfiguration.getClock().setCurrentTime(nextTimeCal.getTime());

        // ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key,variables);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key);


        runtimeService.setVariable(processInstance.getId(), "EndDateForCatch1", dateStr1);
        runtimeService.setVariable(processInstance.getId(), "EndDateForCatch2", dateStr2);

        List<Task> tasks = taskService.createTaskQuery().list();
        assertThat(tasks).hasSize(1);

        tasks = taskService.createTaskQuery().list();
        assertThat(tasks).hasSize(1);
        Task task = tasks.get(0);
        assertThat(task.getName()).isEqualTo("Task A");

        // Test Timer Catch Intermediate Events after completing Task A (endDate not reached but it will be executed according to the expression)
        taskService.complete(task.getId());

        Job timerJob = managementService.createTimerJobQuery().processInstanceId(processInstance.getId()).singleResult();
        assertThat(timerJob).isNotNull();

        JobTestHelper.waitForJobExecutorToProcessAllJobs(processEngineConfiguration, managementService,2000, 100);

        // Expected that job isn't executed because the timer is in t0");
        Job timerJobAfter = managementService.createTimerJobQuery().processInstanceId(processInstance.getId()).singleResult();
        assertThat(timerJobAfter.getId()).isEqualTo(timerJob.getId());

        // nextTimeCal.add(Calendar.HOUR, 1); // after 1 hour and 5 minutes the timer event should be executed.
        nextTimeCal.add(Calendar.MINUTE, 20);
        processEngineConfiguration.getClock().setCurrentTime(nextTimeCal.getTime());

        JobTestHelper.waitForJobExecutorToProcessAllJobs(processEngineConfiguration, managementService,3000, 900);
        // expect to execute because the time is reached.

        List<Job> jobs = managementService.createTimerJobQuery().list();
        assertThat(jobs).hasSize(0);

        tasks = taskService.createTaskQuery().list();
        assertThat(tasks).hasSize(1);
        task = tasks.get(0);
        System.out.println("task name :"+task.getName());
        assertThat(task.getName()).isEqualTo("Task C");

        // Test Timer Catch Intermediate Events after completing Task C
        taskService.complete(task.getId());
        nextTimeCal.add(Calendar.HOUR, 1); // after 1 hour and 5 minutes the timer event should be executed.
        // nextTimeCal.add(Calendar.MINUTE, 18);
        processEngineConfiguration.getClock().setCurrentTime(nextTimeCal.getTime());

        JobTestHelper.waitForJobExecutorToProcessAllJobs(processEngineConfiguration, managementService,3000, 500);
        // expect to execute because the end time is reached.

        if (processEngineConfiguration.getHistoryLevel().isAtLeast(HistoryLevel.ACTIVITY)) {
            HistoricProcessInstance historicInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstance.getId())
                    .singleResult();
            assertThat(historicInstance.getEndTime()).isNotNull();
        }

        // now all the process instances should be completed
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().list();
        assertThat(processInstances).hasSize(0);

        // no jobs
        jobs = managementService.createTimerJobQuery().list();
        assertThat(jobs).hasSize(0);

        // no tasks
        tasks = taskService.createTaskQuery().list();
        assertThat(tasks).hasSize(0);

        return processInstance;
    }

    /**
     * key : boundaryRepeatWithEnd
     */
    public ProcessInstance validateBoundaryTimer(String key){
        Calendar calendar = Calendar.getInstance();
        Date baseTime = calendar.getTime();

        calendar.add(Calendar.MINUTE, 20);
        // expect to stop boundary jobs after 20 minutes
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        DateTime dt = new DateTime(calendar.getTime());
        String dateStr = fmt.print(dt);

        // reset the timer
        Calendar nextTimeCal = Calendar.getInstance();
        nextTimeCal.setTime(baseTime);
        processEngineConfiguration.getClock().setCurrentTime(nextTimeCal.getTime());

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key);

        runtimeService.setVariable(processInstance.getId(), "EndDateForBoundary", dateStr);

        List<Task> tasks = taskService.createTaskQuery().list();
        assertThat(tasks).hasSize(1);

        Task task = tasks.get(0);
        assertThat(task.getName()).isEqualTo("Task A");



        JobTestHelper.waitForJobExecutorToProcessAllJobs(processEngineConfiguration, managementService,2000, 500);



        // Test Boundary Events
        // complete will cause timer to be created
        taskService.complete(task.getId());

        List<Job> jobs = managementService.createTimerJobQuery().list();
        assertThat(jobs).hasSize(1);

        // boundary events
        Job executableJob = managementService.moveTimerToExecutableJob(jobs.get(0).getId());
        managementService.executeJob(executableJob.getId());

        assertThat(managementService.createJobQuery().list()).hasSize(0);
        jobs = managementService.createTimerJobQuery().list();
        assertThat(jobs).hasSize(1);

        nextTimeCal.add(Calendar.MINUTE, 15); // after 15 minutes
        processEngineConfiguration.getClock().setCurrentTime(nextTimeCal.getTime());

        executableJob = managementService.moveTimerToExecutableJob(jobs.get(0).getId());
        managementService.executeJob(executableJob.getId());

        //增加延时
        nextTimeCal.add(Calendar.MINUTE, 5);
        processEngineConfiguration.getClock().setCurrentTime(nextTimeCal.getTime());



        assertThat(managementService.createJobQuery().list()).hasSize(0);
        jobs = managementService.createTimerJobQuery().list();
        assertThat(jobs).hasSize(1);

        nextTimeCal.add(Calendar.MINUTE, 5); // after another 5 minutes (20 minutes and 1 second from the baseTime) the BoundaryEndTime is reached
        nextTimeCal.add(Calendar.SECOND, 1);
        processEngineConfiguration.getClock().setCurrentTime(nextTimeCal.getTime());

        executableJob = managementService.moveTimerToExecutableJob(jobs.get(0).getId());
        managementService.executeJob(executableJob.getId());

        jobs = managementService.createTimerJobQuery().list();
        assertThat(jobs).hasSize(0);
        jobs = managementService.createJobQuery().list();
        assertThat(jobs).hasSize(0);

        tasks = taskService.createTaskQuery().list();
        task = tasks.get(0);
        assertThat(task.getName()).isEqualTo("Task B");
        assertThat(tasks).hasSize(1);
        taskService.complete(task.getId());

        jobs = managementService.createTimerJobQuery().list();
        assertThat(jobs).hasSize(0);
        jobs = managementService.createJobQuery().list();
        assertThat(jobs).hasSize(0);

        if (processEngineConfiguration.getHistoryLevel().isAtLeast(HistoryLevel.ACTIVITY)) {
            HistoricProcessInstance historicInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstance.getId())
                    .singleResult();
            assertThat(historicInstance.getEndTime()).isNotNull();
        }

        // now all the process instances should be completed
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().list();
        assertThat(processInstances).hasSize(0);

        // no jobs
        jobs = managementService.createJobQuery().list();
        assertThat(jobs).hasSize(0);

        jobs = managementService.createTimerJobQuery().list();
        assertThat(jobs).hasSize(0);

        // no tasks
        tasks = taskService.createTaskQuery().list();
        assertThat(tasks).hasSize(0);

        return processInstance;
    }
}
