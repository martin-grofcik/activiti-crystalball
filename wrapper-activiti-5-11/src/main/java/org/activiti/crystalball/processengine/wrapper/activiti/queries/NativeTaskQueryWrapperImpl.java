package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.NativeTaskQueryWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.TaskWrapper;
import org.activiti.engine.task.NativeTaskQuery;
import org.activiti.engine.task.Task;

import java.util.List;


public class NativeTaskQueryWrapperImpl implements NativeTaskQueryWrapper {
    protected final NativeTaskQuery nativeTaskQuery;

    @Override
    public NativeTaskQueryWrapper sql(String selectClause) {
        nativeTaskQuery.sql(selectClause); return this;
    }

    @Override
    public NativeTaskQueryWrapper parameter(String name, Object value) {
        nativeTaskQuery.parameter(name, value); return this;
    }

    @Override
    public long count() {
        return nativeTaskQuery.count();
    }

    @Override
    public TaskWrapper singleResult() {
        return new TaskWrapperImpl(nativeTaskQuery.singleResult());
    }

    @Override
    public List<TaskWrapper> list() {
        return TaskWrapperImpl.convert(nativeTaskQuery.list());
    }

    public NativeTaskQueryWrapperImpl(NativeTaskQuery nativeTaskQuery) {
        this.nativeTaskQuery = nativeTaskQuery;
    }
}
