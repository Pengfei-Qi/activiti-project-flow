package com.fei.activitiprojectflow.vo;

public enum TimerEnum {

    NAME("name","名称"),
    ID("id","id"),
    USER_TASK("userTask","userTask服务"),
    TIMER_INTERMEDIATE_CATCH_EVENT("intermediateCatchEvent","中间定时器事件"),
    TIMER_EVENT_DEFINITION("timerEventDefinition","定时模块"),
    TIMER_BOUNDARY_EVENT("boundaryEvent","边界事件"),
    TIMER_CYCLE("timeCycle","循环周期"),
    TIMER_DURATION("timeDuration","持续时间"),
    TIMER_CYCLE_VARIABLE("${process.timer.timeCycle}","循环周期表达式"),
    TIMER_DURATION_VARIABLE("${process.timer.timeDuration}","持续时间表达式"),
    TIMER_BOUNDARY("B","边界定时器"),
    TIMER_INTERMEDIATE("I", "中间事件定时器");

    public final String expression;
    private final String desc;


    TimerEnum(String expression, String desc) {
        this.expression = expression;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return this.desc;
    }
}
