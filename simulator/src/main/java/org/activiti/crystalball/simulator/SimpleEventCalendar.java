package org.activiti.crystalball.simulator;

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


import org.activiti.engine.impl.util.ClockUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class SimpleEventCalendar implements EventCalendar {

  private static Logger log = LoggerFactory.getLogger(SimpleEventCalendar.class.getName());

  private static final int NULL = -1;

  List<SimulationEvent> eventList = new ArrayList<SimulationEvent>();
	int minIndex = NULL;
	Comparator<SimulationEvent> eventComparator;
	
	public SimpleEventCalendar(Comparator<SimulationEvent> eventComparator) {
		this.eventComparator = eventComparator;		
	}
	
	@Override
    public boolean isEmpty() {
		return minIndex == NULL;
	}

  @Override
  public SimulationEvent peekFirstEvent() {
    if (minIndex == NULL)
      return null;

    return eventList.get((int) minIndex);
  }

  @Override
  public SimulationEvent removeFirstEvent() {
		if (minIndex == NULL)
			return null;
		
		SimulationEvent minEvent = eventList.remove( (int) minIndex );
		
		if (minEvent.hasSimulationTime() && minEvent.getSimulationTime() < ClockUtil.getCurrentTime().getTime())
			throw new RuntimeException("Unable to execute event from the past");
		
		if (eventList.isEmpty()) { 
			minIndex = NULL;
		} else {
			minIndex = 0;
			SimulationEvent event = eventList.get(0);
			for ( int i = 1; i < eventList.size(); i++ ) {
				if (eventComparator.compare( eventList.get( i ), event ) < 0) {
					minIndex = i;
					event = eventList.get( i );
				}
			}
		}
		return minEvent;
	}
	
	@Override
  public void addEvent(SimulationEvent event) {
    log.debug("Scheduling new event [{}]",event);
    if (event != null && isMinimal(event))
			minIndex = eventList.size();
		eventList.add(event);			
	}

  @Override
  public void clear() {
    eventList.clear();
    minIndex = NULL;
  }

  /**
	 * is event the first event in the calendar?
	 * @param event - used in comparison
	 * @return
	 */
	private boolean isMinimal(SimulationEvent event) {
		if (minIndex == NULL)
			return true;
		if ( eventComparator.compare( event, eventList.get( minIndex )) < 0)
			return true;
		return false;
	}

  public void addEvents(Collection<SimulationEvent> simulationEvents) {
    for (SimulationEvent event : simulationEvents) {
      addEvent(event);
    }
  }
}
