package org.springframework.boot.actuate.autoconfigure.metrics.export.simple;

import java.lang.Class;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link SimpleProperties}
 */
public class SimpleProperties__TestContext001_BeanDefinitions {
  /**
   * Get the bean definition for 'simpleProperties'
   */
  public static BeanDefinition getSimplePropertiesBeanDefinition() {
    Class<?> beanType = SimpleProperties.class;
    RootBeanDefinition beanDefinition = new RootBeanDefinition(beanType);
    beanDefinition.setInstanceSupplier(SimpleProperties::new);
    return beanDefinition;
  }
}
