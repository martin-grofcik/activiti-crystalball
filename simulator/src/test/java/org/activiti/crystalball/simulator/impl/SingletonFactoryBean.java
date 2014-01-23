package org.activiti.crystalball.simulator.impl;


import org.activiti.crystalball.simulator.FactoryBean;

public class SingletonFactoryBean<T> implements FactoryBean<T> {
  private T singletonObject;

  public SingletonFactoryBean(FactoryBean<T> factoryBean) {
    singletonObject = factoryBean.getObject();
  }

  @Override
  public T getObject() throws RuntimeException {
    return singletonObject;
  }

  @Override
  public Class<?> getObjectType() {
    return singletonObject.getClass();
  }

  @Override
  public boolean isSingleton() {
    return true;
  }
}
