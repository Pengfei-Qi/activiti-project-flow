package com.fei.activitiprojectflow.activiti.controller;

import com.fei.activitiprojectflow.activiti.pojo.ActivitiVo;
import com.fei.activitiprojectflow.activiti.service.ActivitiService;
import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activiti/")
public class ActivitiController {

    @Autowired
    private ActivitiService activitiService;

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 流程发起页面
     */
    @GetMapping("index")
    public ModelAndView index(){
        return new ModelAndView("index");
    }

    /**
     * 请假流程发起
     */
    @PostMapping("askForLeave")
    public void askForLeave(ActivitiVo activitiVo){
        activitiService.askForLeave(activitiVo);
    }

    /**
     * 查看用户当前任务
     */
        @GetMapping("queryUserTaskByUserName")
    public ModelAndView queryUserTaskByUserName(ActivitiVo activitiVo) {
        ArrayList<Map> list = activitiService.queryUserTaskByUserName(activitiVo);;
        ModelAndView modelAndView = new ModelAndView("taskList", "list", list);
        modelAndView.addObject("username",activitiVo.getUsername());
        return modelAndView;
    }

    /**
     * 查询用户发起的流程
     */
    @GetMapping("getHistoricProcessInstanceByUserName")
    public ModelAndView getHistoricProcessInstance(ActivitiVo activitiVo) {
        ArrayList<Map> list = activitiService.getHistoricProcessInstance(activitiVo);
        ModelAndView modelAndView = new ModelAndView("processInstanceList", "list", list);
        modelAndView.addObject("username",activitiVo.getUsername());
        return modelAndView;
    }

    /**
     * 完成一个任务
     */
    @PostMapping("completeUserTaskById")
    public void completeUserTaskById(ActivitiVo activitiVo) {
        activitiService.completeUserTaskById(activitiVo);
    }

    /**
     * 根据流程实例id查看当前流程图
     */
    @GetMapping("getFlowImgByInstanceId")
    public void getFlowImgByInstanceId(String processInstanceId, HttpServletResponse response) throws IOException  {
        //设置页面不缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        ServletOutputStream outputStream = response.getOutputStream();

        //设置输出的内容的类型为JPEG图像
        response.setContentType("image/svg");

        //写给浏览器
        activitiService.getFlowImgByInstanceId(processInstanceId,outputStream);

        outputStream.flush();
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
