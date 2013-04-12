package org.activiti.spring;

/*
 * #%L
 * simulator
 * %%
 * Copyright (C) 2012 - 2013 crystalball
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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
