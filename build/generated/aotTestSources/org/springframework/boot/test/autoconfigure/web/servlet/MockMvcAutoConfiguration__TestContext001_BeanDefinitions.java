package org.springframework.boot.test.autoconfigure.web.servlet;

import java.lang.Class;
import java.util.List;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.web.context.WebApplicationContext;

/**
 * Bean definitions for {@link MockMvcAutoConfiguration}
 */
public class MockMvcAutoConfiguration__TestContext001_BeanDefinitions {
  /**
   * Get the bean instance supplier for 'org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration'.
   */
  private static BeanInstanceSupplier<MockMvcAutoConfiguration> getMockMvcAutoConfigurationInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<MockMvcAutoConfiguration>forConstructor(WebApplicationContext.class, WebMvcProperties.class)
            .withGenerator((registeredBean, args) -> new MockMvcAutoConfiguration(args.get(0), args.get(1)));
  }

  /**
   * Get the bean definition for 'mockMvcAutoConfiguration'
   */
  public static BeanDefinition getMockMvcAutoConfigurationBeanDefinition() {
    Class<?> beanType = MockMvcAutoConfiguration.class;
    RootBeanDefinition beanDefinition = new RootBeanDefinition(beanType);
    beanDefinition.setInstanceSupplier(getMockMvcAutoConfigurationInstanceSupplier());
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'mockMvcBuilder'.
   */
  private static BeanInstanceSupplier<DefaultMockMvcBuilder> getMockMvcBuilderInstanceSupplier() {
    return BeanInstanceSupplier.<DefaultMockMvcBuilder>forFactoryMethod(MockMvcAutoConfiguration.class, "mockMvcBuilder", List.class)
            .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean(MockMvcAutoConfiguration.class).mockMvcBuilder(args.get(0)));
  }

  /**
   * Get the bean definition for 'mockMvcBuilder'
   */
  public static BeanDefinition getMockMvcBuilderBeanDefinition() {
    Class<?> beanType = DefaultMockMvcBuilder.class;
    RootBeanDefinition beanDefinition = new RootBeanDefinition(beanType);
    beanDefinition.setInstanceSupplier(getMockMvcBuilderInstanceSupplier());
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'springBootMockMvcBuilderCustomizer'.
   */
  private static BeanInstanceSupplier<SpringBootMockMvcBuilderCustomizer> getSpringBootMockMvcBuilderCustomizerInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<SpringBootMockMvcBuilderCustomizer>forFactoryMethod(MockMvcAutoConfiguration.class, "springBootMockMvcBuilderCustomizer")
            .withGenerator((registeredBean) -> registeredBean.getBeanFactory().getBean(MockMvcAutoConfiguration.class).springBootMockMvcBuilderCustomizer());
  }

  /**
   * Get the bean definition for 'springBootMockMvcBuilderCustomizer'
   */
  public static BeanDefinition getSpringBootMockMvcBuilderCustomizerBeanDefinition() {
    Class<?> beanType = SpringBootMockMvcBuilderCustomizer.class;
    RootBeanDefinition beanDefinition = new RootBeanDefinition(beanType);
    beanDefinition.setInstanceSupplier(getSpringBootMockMvcBuilderCustomizerInstanceSupplier());
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'mockMvc'.
   */
  private static BeanInstanceSupplier<MockMvc> getMockMvcInstanceSupplier() {
    return BeanInstanceSupplier.<MockMvc>forFactoryMethod(MockMvcAutoConfiguration.class, "mockMvc", MockMvcBuilder.class)
            .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean(MockMvcAutoConfiguration.class).mockMvc(args.get(0)));
  }

  /**
   * Get the bean definition for 'mockMvc'
   */
  public static BeanDefinition getMockMvcBeanDefinition() {
    Class<?> beanType = MockMvc.class;
    RootBeanDefinition beanDefinition = new RootBeanDefinition(beanType);
    beanDefinition.setInstanceSupplier(getMockMvcInstanceSupplier());
    return beanDefinition;
  }
}
