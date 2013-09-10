package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.CommentWrapper;
import org.activiti.engine.task.Comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentWrapperImpl implements CommentWrapper {
    private final Comment taskComment;

    public String getUserId() {
        return taskComment.getUserId();
    }

    public Date getTime() {
        return taskComment.getTime();
    }

    public String getTaskId() {
        return taskComment.getTaskId();
    }

    public String getFullMessage() {
        return taskComment.getFullMessage();
    }

    public String getProcessInstanceId() {
        return taskComment.getProcessInstanceId();
    }

    public CommentWrapperImpl(Comment taskComment) {
        this.taskComment = taskComment;
    }

    public static List<CommentWrapper> convert(List<Comment> list) {
        if (list != null) {
            List<CommentWrapper> destinationList = new ArrayList<CommentWrapper>(list.size());
            for (Comment item : list) {
                destinationList.add( new CommentWrapperImpl( item ));
            }
            return destinationList;
        }
        return null;
    }
}
