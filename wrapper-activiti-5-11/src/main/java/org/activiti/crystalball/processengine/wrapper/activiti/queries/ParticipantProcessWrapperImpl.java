package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.ParticipantProcessWrapper;
import org.activiti.engine.impl.pvm.process.ParticipantProcess;

public class ParticipantProcessWrapperImpl implements ParticipantProcessWrapper {
    protected final ParticipantProcess participantProcess;

    @Override
    public void setId(String id) {
        participantProcess.setId(id);
    }

    @Override
    public String getId() {
        return participantProcess.getId();
    }

    @Override
    public String getName() {
        return participantProcess.getName();
    }

    @Override
    public void setName(String name) {
        participantProcess.setName(name);
    }

    @Override
    public int getX() {
        return participantProcess.getX();
    }

    @Override
    public void setX(int x) {
        participantProcess.setX(x);
    }

    @Override
    public int getY() {
        return participantProcess.getY();
    }

    @Override
    public void setY(int y) {
        participantProcess.setY(y);
    }

    @Override
    public int getWidth() {
        return participantProcess.getWidth();
    }

    @Override
    public void setWidth(int width) {
        participantProcess.setWidth(width);
    }

    @Override
    public int getHeight() {
        return participantProcess.getHeight();
    }

    @Override
    public void setHeight(int height) {
        participantProcess.setHeight(height);
    }

    public ParticipantProcessWrapperImpl(ParticipantProcess participantProcess) {
        this.participantProcess = participantProcess;
    }
}
