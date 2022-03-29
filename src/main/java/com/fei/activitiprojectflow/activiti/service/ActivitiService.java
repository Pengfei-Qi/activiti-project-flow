package com.fei.activitiprojectflow.activiti.service;



import com.fei.activitiprojectflow.activiti.pojo.ActivitiVo;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ActivitiService {
    void getFlowImgByInstanceId(String processInstanceId, OutputStream outputStream);

    void askForLeave(ActivitiVo activitiVo);

    ArrayList<Map> queryUserTaskByUserName(ActivitiVo activitiVo);

    ArrayList<Map> getHistoricProcessInstance(ActivitiVo activitiVo);

    void completeUserTaskById(ActivitiVo activitiVo);

    List<Map<String, Object>> getAllInstanceInfo();

    List<Map<String, Object>> getNextTaskInfo(String processInstanceId);
}
