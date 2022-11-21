package com.example.restservice;

import java.lang.Class;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ConfigurationClassUtils;

/**
 * Bean definitions for {@link RestServiceApplication}
 */
public class RestServiceApplication__TestContext001_BeanDefinitions {
  /**
   * Get the bean definition for 'restServiceApplication'
   */
  public static BeanDefinition getRestServiceApplicationBeanDefinition() {
    Class<?> beanType = RestServiceApplication.class;
    RootBeanDefinition beanDefinition = new RootBeanDefinition(beanType);
    ConfigurationClassUtils.initializeConfigurationClass(RestServiceApplication.class);
    beanDefinition.setInstanceSupplier(RestServiceApplication$$SpringCGLIB$$0::new);
    return beanDefinition;
  }
}
