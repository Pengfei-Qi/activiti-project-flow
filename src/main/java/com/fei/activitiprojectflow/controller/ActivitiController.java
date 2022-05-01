package com.fei.activitiprojectflow.controller;

import com.fei.activitiprojectflow.service.ActivitiService;
import com.fei.activitiprojectflow.vo.ActivitiVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activiti/")
public class ActivitiController {

    @Autowired
    private ActivitiService activitiService;

    /**
     * 查看用户当前任务
     * @return
     */
    @GetMapping("queryUserTaskByUserName")
    public ArrayList<Map> queryUserTaskByUserName(ActivitiVo activitiVo) {
        return activitiService.queryUserTaskByUserName(activitiVo);
    }

    /**
     * 查询用户发起的流程
     * @return
     */
    @GetMapping("getHistoricProcessInstanceByUserName")
    public ArrayList<Map> getHistoricProcessInstance(ActivitiVo activitiVo) {
        return activitiService.getHistoricProcessInstance(activitiVo);
    }

    /**
     * 完成一个任务
     */
    @PostMapping("completeUserTaskById")
    public void completeUserTaskById(ActivitiVo activitiVo) {
        activitiService.completeUserTaskById(activitiVo);
    }


    /**
     * 获取所有的流程实例
     * @return
     */
    @GetMapping("getAllInstance")
    public List<Map<String, Object>> getAllInstance(){
        return activitiService.getAllInstanceInfo();
    }

    /**
     * 获取任务的下一个节点信息
     * @return
     */
    @GetMapping("getNextTaskInfo")
    public List<Map<String, Object>> getNextTaskInfo(@RequestParam("processInstanceId") String processInstanceId){
        return activitiService.getNextTaskInfo(processInstanceId);
    }
}
