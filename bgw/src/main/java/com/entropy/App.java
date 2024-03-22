package com.entropy;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
        /*String applicationName = context.getEnvironment().getProperty("applicationName");
        System.out.println(applicationName);*/

        // AnnotationConfigServletWebServerApplicationContext 最终获取到的容器类型
        System.out.println(context.getClass());
        App app = context.getBean(App.class);
        System.out.println(app);
    }
}
