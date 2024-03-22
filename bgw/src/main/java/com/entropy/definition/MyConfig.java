package com.entropy.definition;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {

    @Bean // 由 ConfigurationClassBeanDefinition 描述
    public String foo() {
        return "foo";
    }
}
