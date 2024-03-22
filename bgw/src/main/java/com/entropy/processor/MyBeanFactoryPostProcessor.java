package com.entropy.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;

@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    // 当 BeanFactory 被实例化之后 (Bean 创建之前), 回调此函数, 注册一些 BeanDefinition
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 注册一个 Teacher 的 BeanDefinition
        GenericBeanDefinition gBD = new GenericBeanDefinition();
        gBD.setBeanClass(Teacher.class);

        // 向下强转
        DefaultListableBeanFactory dBF = (DefaultListableBeanFactory) beanFactory;
        dBF.registerBeanDefinition("teacher", gBD);
    }
}
