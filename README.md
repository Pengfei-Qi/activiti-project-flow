# Activiti 项目

## 1. 参考资料

1. 官网-1: [7.1.0.M6 - Activiti & Activiti Cloud Developers Guide (gitbook.io)](https://activiti.gitbook.io/activiti-7-developers-guide/releases/7.1.0.m6)
2. 官网-2: [6.0.0 Activiti User Guide](https://www.activiti.org/userguide/)
3. bpmn绘制工具: [BPMN在线绘制工具 (treeants.net)](http://treeants.net/static/bpmn/bpmn.html) 
4. 系统搭建参考: [Springboot整合activiti7（排除springSecurity版）_fanjianglin的博客-CSDN博客_activiti7排除security](https://blog.csdn.net/fanjianglin/article/details/116241928)

## 2. 组件

### 2.1 Service相关

| 名称                | 说明                                                         |
| :------------------ | :----------------------------------------------------------- |
| `ProcessEngine`     | 流程引擎，可以获得其他所有的Service。                        |
| `RepositoryService` | Repository中存储了流程定义文件、部署和支持数据等信息；RepositoryService提供了对repository的存取服务。 |
| `RuntimeService`    | 提供启动流程、查询流程实例、设置获取流程实例变量等功能。     |
| `TaskService`       | 提供运行时任务查询、领取、完成、删除以及变量设置等功能。     |
| `HistoryService`    | 用于获取正在运行或已经完成的流程实例的信息。                 |
| `FormService`       | 提供定制任务表单和存储表单数据的功能，注意存储表单数据可选的功能，也可以向自建数据表中提交数据。 |
| `IdentityService`   | 提供对内建账户体系的管理功能，注意它是可选的服务，可以是用外部账户体系。 |
| `ManagementService` | 较少使用，与流程管理无关，主要用于Activiti系统的日常维护。   |

### 2.2 数据库

- [activiti5.21   -    23张表](https://www.cnblogs.com/zjfjava/p/7110484.html)
- [Activiti6.0-------28张表](https://www.cnblogs.com/c1024/p/11016466.html)
- [Activiti7 ----25张表](https://www.cnblogs.com/flower-dance/p/13576223.html)

Activiti的表都以`act_开头`，第二部分是表示表的用途的两个字母缩写标识，用途也和服务的API对应。
1
`act_hi_*`：'hi’表示 `history`，此前缀的表包含历史数据，如历史(结束)流程实例，变量，任务等等。
`act_ge_*`：'ge’表示 `general`，此前缀的表为通用数据，用于不同场景中。
`act_evt_*`：'evt’表示 `event`，此前缀的表为事件日志。
`act_procdef_*`：'procdef’表示 `processdefine`，此前缀的表为记录流程定义信息。
`act_re_*`：'re’表示 `repository`，此前缀的表包含了流程定义和流程静态资源(图片，规则等等)。
`act_ru_*`：'ru’表示 `runtime`，此前缀的表是记录运行时的数据，包含流程实例，任务，变量，异步任务等运行中的数据。Activiti只在流程实例执行过程中保存这些数据，在流程结束时就会删除这些记录。

### 2.3 idea中的bpmn插件

1. ==2020.x 版本==

   [参考链接-actiBPM 插件](https://blog.csdn.net/shipfei_csdn/article/details/105157702)

   ![插件](https://s3.bmp.ovh/imgs/2022/04/29/556dedf093a6611c.png)

2. ==2021.x及以上版本==

   ![图片一](https://s3.bmp.ovh/imgs/2022/04/29/2878dddabd5a716e.png)

   ![](https://s3.bmp.ovh/imgs/2022/04/29/d12b97311cb0f560.png)

### 2.4 UEL表达式

> 参考链接1: [Activiti User Guide](https://www.activiti.org/userguide/#apiExpressions)
>
> 参考链接2: [Activiti7 UEL表达式 - 掘金 (juejin.cn)](https://juejin.cn/post/6976232222097408031)

![](https://s3.bmp.ovh/imgs/2022/04/29/c5fb7e984e39caf1.png)

### 2.5 开始事件 - Satrt Event

> 参考资料: [Activiti 启动事件（Start Event） - Jesai - 博客园 (cnblogs.com)](https://www.cnblogs.com/dengjiahai/p/7191865.html)

1. `启动事件（Satrt Event）`
2. 定时启动事件（Timer Satrt Event）
3. 信号事件(Signal Start Event)
4. 消息事件(Message Start Event)
5. 异常事件(Error Start Event)

### 2.6 结束事件 - EndEvent 

> 参考链接: [Activiti结束事件（End Event） - Jesai - 博客园 (cnblogs.com)](https://www.cnblogs.com/dengjiahai/p/7287745.html)

1. `结束任务（end event）`
2. 错误结束事件（error end event）
3. 取消结束任务（terminate end event）
4. 终结任务（Cancel end event）

### 2.7 用户任务--userTask

| 参数             | 介绍                               | 
| ---------------- | ---------------------------------- | 
| Assignee         | 流程指派人                         |   
| Candidate Users  | 能够操作的用户列表                 | 
| Candidate Groups | 能够操作节点的用户组               |  
| Due Date         | 到期时间                           | 
| From Key         | 获取任务节点需要展示的页面         | 
| Priority         | 用户节点优先级，用于查询优先级排序 |                                                              |
| Form             | 标签属性，参数列表                 | 

### 2.8  服务任务-serviceTask

> 参考资料: [Activiti中ServiceTask的java服务任务_张志翔 ̮的博客-CSDN博客_activiti service task](https://blog.csdn.net/qq_19734597/article/details/87625155)

| 参数       | 介绍                                  |
| ---------- | ------------------------------------- |
| Fields     | 元素属性，serviceTask表单内字段值解析 |
| Type/Class | 调用的java方法与类型                  |

### 2.9 监听器

> 参考链接: [Activiti6.0（八）监听器使用_清茶_的博客-CSDN博客](https://blog.csdn.net/m0_38001814/article/details/104197670)

- 执行监听器（**ExecutionListener**）
- 任务监听器（TaskListener）
- 事件监听器（ActivitiEventListener）

### 2.10 网关

> 参考链接: [Activiti 网关 - 简书 (jianshu.com)](https://www.jianshu.com/p/1413fe584e47)

- 并行网关(Parallel Gateway)
- 排他网关(Exclusive Gateway)
- 聚合网关(Inclusive Gateway)
- 事件网关(Event-based Gateway)

### 2.11 变量

[（八）Activiti之流程变量和局部流程变量 - shyroke、 - 博客园 (cnblogs.com)](https://www.cnblogs.com/shyroke/p/7995231.html)

1. 全局变量
2. 局部变量

### 2.12 定时任务

- Timer Start Event: 定时任务开始
- Timer Intermediate Catching Event: 中间定时事件
- Timer Boundary Event: 边界定时器事件
- Sub-Process Timer: 子任务定时器

## 3. 运行

> springboot     2.1.7
> activiti       7.1.0.M6

### 3.1 构建bpmn模型

