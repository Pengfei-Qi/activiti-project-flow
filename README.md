## 一. **工程简介**

>  activity 流程图-练手项目

## **二. 延伸阅读:**

```shell
springboot     2.1.7
activiti       7.1.0.M6
```

## **三. 测试项目:**

1. `User Task测试:`    审批/查询/撤销/挂起/恢复
2. `Service Task测试 :`    通过实现javaDelegate执行任务
3. `网关测试:` 
   - 并行网关(Parallel Gateway)
   - 排他网关(Exclusive Gateway)
   - 聚合网关(Inclusive Gateway)
   - 事件网关(Event-based Gateway)
4. `定时任务(Timer): `
   - Timer Start Event: 定时任务开始
   -  Timer Intermediate Catching Event: 中间定时事件
   - Timer Boundary Event: 边界定时器事件
   -  Sub-Process Timer: 子任务定时器
5. `监听器测试:`            [参考链接](https://blog.csdn.net/m0_38001814/article/details/104197670)
   - ExecutionListener: 执行监听器
   - TaskListener: 任务监听器
   - ActivitiEventListener: 事件监听器
6. `变量测试:`
   - 变量: 变量的定义和使用
   - 变量的获取: 变量的获取