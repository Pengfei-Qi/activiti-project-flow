package com.fei.activitiprojectflow.servicetask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * @description:  测试线程安全, 多例
 * @author: qpf
 * @date: 2021/12/29
 * @version: 1.0
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class PrototypeDelegateExpressionBean implements JavaDelegate {
    public static AtomicInteger INSTANCE_COUNT = new AtomicInteger(0);

    private Expression fieldA;
    private Expression fieldB;
    private Expression resultVariableName;



    public PrototypeDelegateExpressionBean() {
        INSTANCE_COUNT.incrementAndGet();
    }

    @Override
    public void execute(DelegateExecution execution) {
        Number fieldAValue = (Number) fieldA.getValue(execution);
        Number fieldValueB = (Number) fieldB.getValue(execution);

        int result = fieldAValue.intValue() + fieldValueB.intValue();
        execution.setVariable(resultVariableName.getValue(execution).toString(), result);
        System.out.println("Prototype        "+resultVariableName.getValue(execution).toString() +"  :  " + result);

        //获取结果变量值
        try {
            System.out.println((String) execution.getVariable("myLetter"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
