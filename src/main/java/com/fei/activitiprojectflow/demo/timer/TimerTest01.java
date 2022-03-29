package com.fei.activitiprojectflow.demo.timer;

import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.Deployment;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @description:
 * @author: qpf
 * @date: 2021/12/31
 * @version: 1.0
 */
public class TimerTest01 {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    ManagementService managementService;

    private Date testExpression(String timeExpression) {
        // Set the clock fixed
        HashMap<String, Object> variables1 = new HashMap<String, Object>();
        variables1.put("dueDate", timeExpression);

        // After process start, there should be timer created
        ProcessInstance pi1 = runtimeService.startProcessInstanceByKey("intermediateTimerEventExample", variables1);
        assertThat(managementService.createTimerJobQuery().processInstanceId(pi1.getId()).count()).isEqualTo(1);

        List<Job> jobs = managementService.createTimerJobQuery().executable().list();
        assertThat(jobs).hasSize(1);
        return jobs.get(0).getDuedate();
    }

    @Deployment
    public void testTimeExpressionComplete() throws Exception {

        System.out.println("定时任务开始执行....................");

        Date dt = new Date();

        Date dueDate = testExpression(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dt));
        assertThat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dueDate)).isEqualTo(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dt));
    }
}
