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
package org.activiti.engine.impl.util;

import java.util.Date;


/**
 * We have to override activiti engine clock utils. Each activiti engine 
 * can live in different simulation time. Usage of overwritten ClockUtils 
 * is based on assumption that class loader will load this implementation first  
 * Simulation time is unique for the thread
 */
public class ClockUtil {
  private volatile static ThreadLocal<Date> CURRENT_TIME = new ThreadLocal<Date>();
  
  public static void setCurrentTime(Date currentTime) {
    CURRENT_TIME.set( currentTime);
  }
  
  public static void reset() {
    CURRENT_TIME.set(null);
  } 
  
  public static Date getCurrentTime() {
    if (CURRENT_TIME.get() != null) {
      return CURRENT_TIME.get();
    }
    return new Date();
  }

}
