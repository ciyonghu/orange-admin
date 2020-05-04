package com.orange.admin.common.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Spring 系统启动应用感知对象，主要用于获取Spring Bean的上下文对象，后续的代码中可以直接查找系统中加载的Bean对象。
 *
 * @author Stephen.Liu
 * @date 2020-04-11
 */
@Component
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * Spring 启动的过程中会自动调用，并将应用上下文对象赋值进来。
     *
     * @param applicationContext 应用上下文对象，可通过该对象查找Spring中已经加载的Bean。
     * @throws BeansException Bean处理相关的异常。
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHolder.applicationContext = applicationContext;
    }

    /**
     * 获取应用上下文对象。
     *
     * @return 应用上下文。
     */
    public static ApplicationContext getApplicationContext() {
        assertApplicationContext();
        return applicationContext;
    }

    /**
     * 根据BeanName，获取Bean对象。
     *
     * @param beanName Bean名称。
     * @param <T> 返回的Bean类型。
     * @return Bean对象。
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        assertApplicationContext();
        return (T) applicationContext.getBean(beanName);
    }

    /**
     * 根据Bean的ClassType，获取Bean对象。
     *
     * @param beanType Bean的Class类型。。
     * @param <T> 返回的Bean类型。
     * @return Bean对象。
     */
    public static <T> T getBean(Class<T> beanType) {
        assertApplicationContext();
        return applicationContext.getBean(beanType);
    }

    private static void assertApplicationContext() {
        if (ApplicationContextHolder.applicationContext == null) {
            throw new RuntimeException("applicaitonContext属性为null,请检查是否注入了ApplicationContextHolder!");
        }
    }

}