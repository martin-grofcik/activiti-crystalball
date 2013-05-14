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


import java.util.Stack;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;

/**
 * Context in which simulation is run.
 * It contains references to process engine, event calendar.
 *
 */
public abstract class SimulationRunContext {
    
	//
	//  Process engine on which simulation will be executed
	//
	protected static ThreadLocal<Stack<RuntimeService>> runtimeServiceThreadLocal = new ThreadLocal<Stack<RuntimeService>>();
	protected static ThreadLocal<Stack<TaskService>> taskServiceThreadLocal = new ThreadLocal<Stack<TaskService>>();
	protected static ThreadLocal<Stack<HistoryService>> historyServiceThreadLocal = new ThreadLocal<Stack<HistoryService>>();

    //
    // Simulation objects
    //
	protected static ThreadLocal<Stack<EventCalendar>> eventCalendarThreadLocal = new ThreadLocal<Stack<EventCalendar>>();
    
	public static RuntimeService getRuntimeService() {
		Stack<RuntimeService> stack = getStack(runtimeServiceThreadLocal);
		if (stack.isEmpty()) {
			return null;
		}
		return stack.peek();
	}

	public static void setRuntimeService(RuntimeService runtimeService) {
	    getStack(runtimeServiceThreadLocal).push(runtimeService);
	}

	public static void removeRuntimeService() {
	    getStack(runtimeServiceThreadLocal).pop();
	}

	public static TaskService getTaskService() {
		Stack<TaskService> stack = getStack(taskServiceThreadLocal);
		if (stack.isEmpty()) {
			return null;
		}
		return stack.peek();
	}

	public static void setTaskService(TaskService taskService) {
	    getStack(taskServiceThreadLocal).push(taskService);
	}

	public static void removeTaskService() {
	    getStack(taskServiceThreadLocal).pop();
	}
	
	public static EventCalendar getEventCalendar() {
		Stack<EventCalendar> stack = getStack(eventCalendarThreadLocal);
		if (stack.isEmpty()) {
			return null;
		}
		return stack.peek();
	}

	public static void setEventCalendar(EventCalendar eventCalendar) {
	    getStack(eventCalendarThreadLocal).push(eventCalendar);
	}

	public static void removeEventCalendar() {
	    getStack(eventCalendarThreadLocal).pop();
	}

	public static HistoryService getHistoryService() {
		Stack<HistoryService> stack = getStack(historyServiceThreadLocal);
		if (stack.isEmpty()) {
			return null;
		}
		return stack.peek();
	}

	public static void setHistoryService(HistoryService historyService) {
	    getStack(historyServiceThreadLocal).push(historyService);
	}

	public static void removeHistoryService() {
	    getStack(historyServiceThreadLocal).pop();
	}
		
	protected static <T> Stack<T> getStack(ThreadLocal<Stack<T>> threadLocal) {
		Stack<T> stack = threadLocal.get();
		if (stack == null) {
			stack = new Stack<T>();
			threadLocal.set(stack);
		}
		return stack;
	}
}
