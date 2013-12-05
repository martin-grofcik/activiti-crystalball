package org.activiti.crystalball.simulator;

/**
 * @see org.springframework.beans.factory.FactoryBean
 *
 */
public interface FactoryBean<T> {
  T getObject() throws RuntimeException;

  Class<?> getObjectType();

  boolean isSingleton();

}

