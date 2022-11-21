package org.springframework.boot.actuate.autoconfigure.endpoint.jackson;

import java.lang.Class;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.actuate.endpoint.jackson.EndpointObjectMapper;

/**
 * Bean definitions for {@link JacksonEndpointAutoConfiguration}
 */
public class JacksonEndpointAutoConfiguration__TestContext001_BeanDefinitions {
  /**
   * Get the bean definition for 'jacksonEndpointAutoConfiguration'
   */
  public static BeanDefinition getJacksonEndpointAutoConfigurationBeanDefinition() {
    Class<?> beanType = JacksonEndpointAutoConfiguration.class;
    RootBeanDefinition beanDefinition = new RootBeanDefinition(beanType);
    beanDefinition.setInstanceSupplier(JacksonEndpointAutoConfiguration::new);
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'endpointObjectMapper'.
   */
  private static BeanInstanceSupplier<EndpointObjectMapper> getEndpointObjectMapperInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<EndpointObjectMapper>forFactoryMethod(JacksonEndpointAutoConfiguration.class, "endpointObjectMapper")
            .withGenerator((registeredBean) -> registeredBean.getBeanFactory().getBean(JacksonEndpointAutoConfiguration.class).endpointObjectMapper());
  }

  /**
   * Get the bean definition for 'endpointObjectMapper'
   */
  public static BeanDefinition getEndpointObjectMapperBeanDefinition() {
    Class<?> beanType = EndpointObjectMapper.class;
    RootBeanDefinition beanDefinition = new RootBeanDefinition(beanType);
    beanDefinition.setInstanceSupplier(getEndpointObjectMapperInstanceSupplier());
    return beanDefinition;
  }
}
