package io.lance.common.core.spring;


import io.lance.common.core.exception.EbsException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 获取bean
 */
public class SpringApplicationContext implements ApplicationContextAware {

    private static ConfigurableApplicationContext applicationContext;

    /**
     *
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringApplicationContext.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    /**
     * 获取bean
     *
     * @param name
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 获取bean
     *
     * @param clazz
     */
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return (T) applicationContext.getBean(clazz);
    }

    /**
     * @desc:获取bean
     * @author lance
     * @time: 2017-08-08 16:19:01
     */
    public static <T> T getBean(String name, Class<T> requiredType) {
        checkApplicationContext();
        return applicationContext.getBean(name, requiredType);
    }

    /**
     * @desc:检查上下文是否注入
     * @author lance
     * @time: 2017-08-08 16:20:09
     */
    private static void checkApplicationContext() {
        if (null == applicationContext)
            throw new EbsException("applicaitonContext未注入,请在spring配置文件中定义SpringContextHolder");
    }

    /**
     * @desc:是否单例
     * @author lance
     * @time: 2017-08-08 16:16:21
     */
    public static boolean isSingleton(String name) {
        checkApplicationContext();
        return applicationContext.isSingleton(name);
    }


}
