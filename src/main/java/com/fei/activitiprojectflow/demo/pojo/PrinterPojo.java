package com.fei.activitiprojectflow.demo.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.activiti.engine.delegate.DelegateExecution;

import java.io.Serializable;

/**
 * @description:
 * @author: qpf
 * @date: 2021/12/29
 * @version: 1.0
 */
@Data
@ToString
@NoArgsConstructor
public class PrinterPojo implements Serializable {

    private Long id = 1000L;

    private String name  = "孙悟空";

    public void printMessage(){
        System.out.println("id: " + this.id + "   name: " + this.name);
    }

    public void printMessage(DelegateExecution execution , String str){
        String letter = (String) execution.getVariable(str);
        System.out.println("从全局变量中取值letter:    "+letter);
        System.out.println("id: " + this.id + "   name: " + this.name);
    }
    public String getMessage(){
        return "id: " + this.id + "   name: " + this.name;
    }


}
