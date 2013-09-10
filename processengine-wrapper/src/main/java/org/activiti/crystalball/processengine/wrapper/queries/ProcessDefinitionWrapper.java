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
package org.activiti.crystalball.processengine.wrapper.queries;


import java.util.List;

/**
 */
public interface ProcessDefinitionWrapper {

  /** unique identifier */
  String getId();

  /** category name which is derived from the targetNamespace attribute in the definitions element */
  String getCategory();
  
  /** label used for display purposes */
  String getName();
  
  /** unique name for all versions this process definitions */
  String getKey();
  
  /** description of this process **/
  String getDescription();
  
  /** version of this process definition */
  int getVersion();

    /**
     *
     * @return
     */
  String getResourceName();

  /** The deployment in which this process definition is contained. */
  String getDeploymentId();
  
  /** The resource name in the deployment of the diagram image (if any). */
  String getDiagramResourceName();

  boolean hasStartFormKey();
  
  /** Returns true if the process definition is in suspended state. */
  boolean isSuspended();

    List<ActivityWrapper> getActivities();

    ParticipantProcessWrapper getParticipantProcess();

    public List<LaneSetWrapper> getLaneSets();
}
