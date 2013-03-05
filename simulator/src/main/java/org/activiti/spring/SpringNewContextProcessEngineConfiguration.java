package org.activiti.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.interceptor.CommandContextInterceptor;
import org.activiti.engine.impl.interceptor.CommandInterceptor;
import org.activiti.engine.impl.interceptor.LogInterceptor;
import org.springframework.transaction.support.TransactionTemplate;

public class SpringNewContextProcessEngineConfiguration extends	SpringProcessEngineConfiguration {
	
	  protected Collection< ? extends CommandInterceptor> getDefaultCommandInterceptorsTxRequired() {
		    if (transactionManager==null) {
		      throw new ActivitiException("transactionManager is required property for SpringProcessEngineConfiguration, use "+StandaloneProcessEngineConfiguration.class.getName()+" otherwise");
		    }
		    
		    List<CommandInterceptor> defaultCommandInterceptorsTxRequired = new ArrayList<CommandInterceptor>();
		    defaultCommandInterceptorsTxRequired.add(new LogInterceptor());
		    defaultCommandInterceptorsTxRequired.add(new SpringTransactionInterceptor(transactionManager, TransactionTemplate.PROPAGATION_REQUIRED));
		    CommandContextInterceptor commandContextInterceptor = new CommandContextInterceptor(commandContextFactory, this);
		    // there is only this change against SpringProcessEngineConfiguration
		    commandContextInterceptor.setContextReusePossible(false);
		    defaultCommandInterceptorsTxRequired.add(commandContextInterceptor);
		    return defaultCommandInterceptorsTxRequired;
	}
}
