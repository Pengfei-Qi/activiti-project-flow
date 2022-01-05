package com.fei.activitiprojectflow.servicetask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateHelper;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:  serviceTask 测试线程安全
 * @author: qpf
 * @date: 2021/12/29
 * @version: 1.0
 */
@Component
public class SingletonDelegateExpressionBean implements JavaDelegate {

    public static AtomicInteger INSTANCE_COUNT = new AtomicInteger(0);

    public SingletonDelegateExpressionBean() {
        INSTANCE_COUNT.incrementAndGet();
    }

    @Override
    public void execute(DelegateExecution execution) {
        Expression fieldAExpression = DelegateHelper.getFieldExpression(execution, "fieldA");
        Number fieldA = (Number) fieldAExpression.getValue(execution);

        Expression fieldBExpression = DelegateHelper.getFieldExpression(execution, "fieldB");
        Number fieldB = (Number) fieldBExpression.getValue(execution);

        int result = fieldA.intValue() + fieldB.intValue();

        String resultVariableName = DelegateHelper.getFieldExpression(execution, "resultVariableName").getValue(execution).toString();
        execution.setVariable(resultVariableName, result);

        System.out.println("Singleton     "+resultVariableName +"  :  " + result);
    }
}
