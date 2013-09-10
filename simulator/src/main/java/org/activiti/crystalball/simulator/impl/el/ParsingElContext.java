/* Licensed under the Apache License, Version 2.0 (the "License");
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
 */
package org.activiti.crystalball.simulator.impl.el;

import org.activiti.crystalball.simulator.impl.javax.el.ELContext;
import org.activiti.crystalball.simulator.impl.javax.el.ELResolver;
import org.activiti.crystalball.simulator.impl.javax.el.FunctionMapper;
import org.activiti.crystalball.simulator.impl.javax.el.VariableMapper;


/**
 * Simple implementation of the {@link org.activiti.engine.impl.javax.el.ELContext} used during parsings.
 *
 * Currently this implementation does nothing, but a non-null implementation
 * of the {@link org.activiti.engine.impl.javax.el.ELContext} interface is required by the {@link org.activiti.engine.impl.javax.el.ExpressionFactory}
 * when create value- and methodexpressions.
 *
 * @see org.activiti.crystalball.simulator.impl.el.ExpressionManager#createExpression(String)
 * @see org.activiti.crystalball.simulator.impl.el.ExpressionManager#createMethodExpression(String)
 * 
 * @author Joram Barrez
 */
public class ParsingElContext extends ELContext {

  public ELResolver getELResolver() {
    return null;
  }

  public FunctionMapper getFunctionMapper() {
    return null;
  }

  public VariableMapper getVariableMapper() {
    return null;
  }

}
