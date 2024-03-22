package com.entropy.aware;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Component;

@Component
public class Student implements BeanNameAware, BeanClassLoaderAware, BeanFactoryAware {
    // BeanClassLoaderAware 的回调方法
    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("Student...setBeanClassLoader..." + classLoader);
    }

    // BeanFactoryAware 的回调方法
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("Student...setBeanFactory..." + beanFactory);
    }

    // BeanNameAware 的回调方法
    @Override
    public void setBeanName(String name) {
        System.out.println("Student...setBeanName..." + name);
    }
}
