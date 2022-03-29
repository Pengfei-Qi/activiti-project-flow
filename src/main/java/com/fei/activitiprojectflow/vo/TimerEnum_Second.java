package com.fei.activitiprojectflow.vo;

public enum TimerEnum_Second {

    NAME("name"),
    ID("id"),
    USER_TASK("userTask"),
    TIMER_INTERMEDIATE_CATCH_EVENT("intermediateCatchEvent"),
    TIMER_EVENT_DEFINITION("timerEventDefinition"),
    TIMER_BOUNDARY_EVENT("boundaryEvent"),
    TIMER_CYCLE("timeCycle"),
    TIMER_DURATION("timeDuration"),
    TIMER_CYCLE_VARIABLE("${process.timer.timeCycle}"),
    TIMER_DURATION_VARIABLE("${process.timer.timeDuration}"),
    TIMER_BOUNDARY("B"),
    TIMER_INTERMEDIATE("I");

    public final String englishWord;

    TimerEnum_Second(String englishWord) {
        this.englishWord = englishWord;
    }

    @Override
    public String toString() {
        return "TimerEnum_Second{" +
                "englishWord='" + englishWord + '\'' +
                '}';
    }
}
