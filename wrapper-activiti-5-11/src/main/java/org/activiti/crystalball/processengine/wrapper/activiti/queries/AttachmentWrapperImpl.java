package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.AttachmentWrapper;
import org.activiti.engine.task.Attachment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: martin.grofcik
 * Date: 10.9.2013
 * Time: 10:35
 * To change this template use File | Settings | File Templates.
 */
public class AttachmentWrapperImpl implements AttachmentWrapper {
    protected final Attachment attachment;

    @Override
    public String getId() {
        return attachment.getId();
    }

    @Override
    public String getName() {
        return attachment.getName();
    }

    @Override
    public void setName(String name) {
        attachment.setName(name);
    }

    @Override
    public String getDescription() {
        return attachment.getDescription();
    }

    @Override
    public void setDescription(String description) {
        attachment.setDescription(description);
    }

    @Override
    public String getType() {
        return attachment.getType();
    }

    @Override
    public String getTaskId() {
        return attachment.getTaskId();
    }

    @Override
    public String getProcessInstanceId() {
        return attachment.getProcessInstanceId();
    }

    @Override
    public String getUrl() {
        return attachment.getUrl();
    }

    public AttachmentWrapperImpl(Attachment attachment) {
        this.attachment = attachment;
    }
    public static List<AttachmentWrapper> convert(List<Attachment> list) {
        if (list != null) {
            List<AttachmentWrapper> destinationList = new ArrayList<AttachmentWrapper>(list.size());
            for (Attachment item : list) {
                destinationList.add( new AttachmentWrapperImpl( item ));
            }
            return destinationList;
        }
        return null;
    }

    public Attachment getAttachment() {
        return attachment;
    }
}
