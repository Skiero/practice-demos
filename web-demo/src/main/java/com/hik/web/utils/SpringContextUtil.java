package com.hik.web.utils;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Spring应用上下文工具类
 *
 * @author wangjinchang5
 * @date 2020/9/18 16:30
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private SpringContextUtil() {
    }

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 根据BeanName从BeanFactory中获取一个实例
     *
     * @param beanName beanName
     * @return 实例
     */
    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    /**
     * 根据BeanClass从BeanFactory中获取唯一实例
     *
     * @param tClass beanClass
     * @param <T>    bean泛型
     * @return 实例
     */
    public static <T> T getBean(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }

    /**
     * 根据BeanName和BeanClass从BeanFactory中获取唯一实例
     *
     * @param beanName  beanName
     * @param beanClass beanClass
     * @param <T>       bean泛型
     * @return 实例
     */
    public static <T> T getBean(String beanName, Class<T> beanClass) {
        return applicationContext.getBean(beanName, beanClass);
    }

    /**
     * 根据i18n国际化文件的key获取value
     *
     * @param i18nKey i18nKey
     * @return i18nValue
     */
    public static String getMessage(String i18nKey) {
        return applicationContext.getMessage(i18nKey, null, LocaleContextHolder.getLocale());
    }

    /**
     * 根据i18n国际化文件的key获取value，如果value不存在，则使用默认value
     *
     * @param i18nKey        i18nKey
     * @param defaultMessage 默认value
     * @return i18nValue
     */
    public static String getMessage(String i18nKey, String defaultMessage) {
        return applicationContext.getMessage(i18nKey, null, defaultMessage, LocaleContextHolder.getLocale());
    }

    /**
     * 根据i18n国际化文件的key获取value，并且在国际化中使用索引接收数据，例如在国际化中使用{0}来接收第一个参数
     *
     * @param i18nKey i18nKey
     * @param args    用于填充的参数，具体使用方法见其接口文档
     * @return i18nValue
     */
    public static String getMessage(String i18nKey, Object[] args) {
        return applicationContext.getMessage(i18nKey, args, LocaleContextHolder.getLocale());
    }

    /**
     * 根据beanName判断BeanFactory中是否存在该bean
     *
     * @param beanName beanName
     * @return true=存在 false=不存在
     */
    public static boolean containsBean(String beanName) {
        return applicationContext.containsBean(beanName);
    }

    /**
     * 根据beanName判断是否为多例
     *
     * @param beanName beanName
     * @return true=多例 false=单例
     */
    public static boolean isPrototype(String beanName) {
        return applicationContext.isPrototype(beanName);
    }

    /**
     * 获取代理对象（前提条件是暴露了代理）
     *
     * @param object 被代理对象
     * @return 代理对象
     */
    public static Object getAopProxyInstance(Object object) {
        EnableAspectJAutoProxy annotation =
                applicationContext.findAnnotationOnBean("webDemoApplication", EnableAspectJAutoProxy.class);
        if (null == annotation || !annotation.exposeProxy()) {
            throw new IllegalStateException("EnableAspectJAutoProxy is missing or expose proxy do not available.");
        }

        Assert.notNull(object, "The current object cannot be null.");
        return AopContext.currentProxy();
    }
}
