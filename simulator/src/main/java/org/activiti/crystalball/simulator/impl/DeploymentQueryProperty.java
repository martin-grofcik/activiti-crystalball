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

package org.activiti.crystalball.simulator.impl;



import org.activiti.engine.query.QueryProperty;

import java.util.HashMap;
import java.util.Map;


/**
 * Contains the possible properties that can be used in a {@link org.activiti.engine.repository.DeploymentQuery}.
 * 
 * @author Joram Barrez
 */
public class DeploymentQueryProperty implements QueryProperty {

  private static final long serialVersionUID = 1L;

  private static final Map<String, DeploymentQueryProperty> properties = new HashMap<String, DeploymentQueryProperty>();

  public static final org.activiti.engine.impl.DeploymentQueryProperty DEPLOYMENT_ID = new org.activiti.engine.impl.DeploymentQueryProperty("RES.ID_");
  public static final org.activiti.engine.impl.DeploymentQueryProperty DEPLOYMENT_NAME = new org.activiti.engine.impl.DeploymentQueryProperty("RES.NAME_");
  public static final org.activiti.engine.impl.DeploymentQueryProperty DEPLOY_TIME = new org.activiti.engine.impl.DeploymentQueryProperty("RES.DEPLOY_TIME_");
  
  private String name;

  public DeploymentQueryProperty(String name) {
    this.name = name;
    properties.put(name, this);
  }

  public String getName() {
    return name;
  }
  
  public static DeploymentQueryProperty findByName(String propertyName) {
    return properties.get(propertyName);
  }

  
}
