package com.fei.activitiprojectflow.service;



import com.fei.activitiprojectflow.vo.ActivitiVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ActivitiService {

    ArrayList<Map> queryUserTaskByUserName(ActivitiVo activitiVo);

    ArrayList<Map> getHistoricProcessInstance(ActivitiVo activitiVo);

    void completeUserTaskById(ActivitiVo activitiVo);

    List<Map<String, Object>> getAllInstanceInfo();

    List<Map<String, Object>> getNextTaskInfo(String processInstanceId);
}
