package org.springframework.boot.actuate.autoconfigure.metrics.export.simple;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.simple.SimpleConfig;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import java.lang.Class;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link SimpleMetricsExportAutoConfiguration}
 */
public class SimpleMetricsExportAutoConfiguration__TestContext001_BeanDefinitions {
  /**
   * Get the bean definition for 'simpleMetricsExportAutoConfiguration'
   */
  public static BeanDefinition getSimpleMetricsExportAutoConfigurationBeanDefinition() {
    Class<?> beanType = SimpleMetricsExportAutoConfiguration.class;
    RootBeanDefinition beanDefinition = new RootBeanDefinition(beanType);
    beanDefinition.setInstanceSupplier(SimpleMetricsExportAutoConfiguration::new);
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'simpleMeterRegistry'.
   */
  private static BeanInstanceSupplier<SimpleMeterRegistry> getSimpleMeterRegistryInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<SimpleMeterRegistry>forFactoryMethod(SimpleMetricsExportAutoConfiguration.class, "simpleMeterRegistry", SimpleConfig.class, Clock.class)
            .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean(SimpleMetricsExportAutoConfiguration.class).simpleMeterRegistry(args.get(0), args.get(1)));
  }

  /**
   * Get the bean definition for 'simpleMeterRegistry'
   */
  public static BeanDefinition getSimpleMeterRegistryBeanDefinition() {
    Class<?> beanType = SimpleMeterRegistry.class;
    RootBeanDefinition beanDefinition = new RootBeanDefinition(beanType);
    beanDefinition.setDestroyMethodNames("close");
    beanDefinition.setInstanceSupplier(getSimpleMeterRegistryInstanceSupplier());
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'simpleConfig'.
   */
  private static BeanInstanceSupplier<SimpleConfig> getSimpleConfigInstanceSupplier() {
    return BeanInstanceSupplier.<SimpleConfig>forFactoryMethod(SimpleMetricsExportAutoConfiguration.class, "simpleConfig", SimpleProperties.class)
            .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean(SimpleMetricsExportAutoConfiguration.class).simpleConfig(args.get(0)));
  }

  /**
   * Get the bean definition for 'simpleConfig'
   */
  public static BeanDefinition getSimpleConfigBeanDefinition() {
    Class<?> beanType = SimpleConfig.class;
    RootBeanDefinition beanDefinition = new RootBeanDefinition(beanType);
    beanDefinition.setInstanceSupplier(getSimpleConfigInstanceSupplier());
    return beanDefinition;
  }
}
