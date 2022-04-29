package com.fei.activitiprojectflow;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ActivitiProjectFlowApplication {

    static {
        System.setProperty("druid.mysql.usePingMethod","false");
    }


    public static void main(String[] args) {
        SpringApplication.run(ActivitiProjectFlowApplication.class, args);
    }

    /**
     * 启动成功
     */
    @Bean
    public ApplicationRunner applicationRunner() {
        return applicationArguments -> {
            System.out.println("启动成功！");
        };
    }

}
