package com.example.restservice;

import java.lang.Class;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link GreetingController}
 */
public class GreetingController__BeanDefinitions {
  /**
   * Get the bean definition for 'greetingController'
   */
  public static BeanDefinition getGreetingControllerBeanDefinition() {
    Class<?> beanType = GreetingController.class;
    RootBeanDefinition beanDefinition = new RootBeanDefinition(beanType);
    beanDefinition.setInstanceSupplier(GreetingController::new);
    return beanDefinition;
  }
}
