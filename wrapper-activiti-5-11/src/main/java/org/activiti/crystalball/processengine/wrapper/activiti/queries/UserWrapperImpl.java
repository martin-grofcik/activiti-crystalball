package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.UserWrapper;
import org.activiti.engine.identity.User;

import java.util.ArrayList;
import java.util.List;

public class UserWrapperImpl implements UserWrapper {
    protected final User user;

    @Override
    public String getId() {
        return user.getId();
    }

    @Override
    public void setId(String id) {
        user.setId(id);
    }

    @Override
    public String getFirstName() {
        return user.getFirstName();
    }

    @Override
    public void setFirstName(String firstName) {
        user.setFirstName(firstName);
    }

    @Override
    public void setLastName(String lastName) {
        user.setLastName(lastName);
    }

    @Override
    public String getLastName() {
        return user.getLastName();
    }

    @Override
    public void setEmail(String email) {
        user.setEmail(email);
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public void setPassword(String string) {
        user.setPassword(string);
    }

    public UserWrapperImpl( User user) {
        this.user = user;
    }

    public static List<UserWrapper> convert(List<User> list) {
        if (list != null) {
            List<UserWrapper> destinationList = new ArrayList<UserWrapper>(list.size());
            for (User item : list) {
                destinationList.add( new UserWrapperImpl( item ));
            }
            return destinationList;
        }
        return null;
    }

    public User getUser() {
        return user;
    }
}
