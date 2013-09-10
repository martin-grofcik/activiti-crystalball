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

/**
 * Allows to programmatically query for {@link GroupWrapper}s.
 * 
 * @author Joram Barrez
 */
public interface GroupQueryWrapper extends Query<GroupQueryWrapper, GroupWrapper> {
  
  /** Only select {@link GroupWrapper}s with the given id. */
  GroupQueryWrapper groupId(String groupId);
  
  /** Only select {@link GroupWrapper}s with the given name. */
  GroupQueryWrapper groupName(String groupName);
  
  /** Only select {@link GroupWrapper}s where the name matches the given parameter.
   *  The syntax to use is that of SQL, eg. %activiti%. */
  GroupQueryWrapper groupNameLike(String groupNameLike);
  
  /** Only select {@link GroupWrapper}s which have the given type. */
  GroupQueryWrapper groupType(String groupType);
  
  /** Only selects {@link GroupWrapper}s where the given user is a member of. */
  GroupQueryWrapper groupMember(String groupMemberUserId);
  
  /** Only select {@link GroupWrapper}S that are potential starter for the given process definition. */
  GroupQueryWrapper potentialStarter(String procDefId);

  
  //sorting ////////////////////////////////////////////////////////
  
  /** Order by group id (needs to be followed by {@link #asc()} or {@link #desc()}). */
  GroupQueryWrapper orderByGroupId();
  
  /** Order by group name (needs to be followed by {@link #asc()} or {@link #desc()}). */
  GroupQueryWrapper orderByGroupName();
  
  /** Order by group type (needs to be followed by {@link #asc()} or {@link #desc()}). */
  GroupQueryWrapper orderByGroupType();

}
