package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.EventWrapper;
import org.activiti.engine.task.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventWrapperImpl implements EventWrapper {
    private final Event event;

    @Override
    public String getAction() {
        return event.getAction();
    }

    @Override
    public List<String> getMessageParts() {
        return event.getMessageParts();
    }

    @Override
    public String getMessage() {
        return event.getMessage();
    }

    @Override
    public String getUserId() {
        return event.getUserId();
    }

    @Override
    public Date getTime() {
        return event.getTime();
    }

    @Override
    public String getTaskId() {
        return event.getTaskId();
    }

    @Override
    public String getProcessInstanceId() {
        return event.getProcessInstanceId();
    }

    public EventWrapperImpl(Event event) {
        this.event = event;
    }

    public static List<EventWrapper> convert(List<Event> list) {
        if (list != null) {
            List<EventWrapper> destinationList = new ArrayList<EventWrapper>(list.size());
            for (Event item : list) {
                destinationList.add( new EventWrapperImpl( item ));
            }
            return destinationList;
        }
        return null;

    }
}
