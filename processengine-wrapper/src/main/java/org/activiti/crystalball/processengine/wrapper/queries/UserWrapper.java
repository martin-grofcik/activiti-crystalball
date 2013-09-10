package org.activiti.crystalball.processengine.wrapper.queries;

import java.io.Serializable;

public interface UserWrapper extends Serializable {

    String getId();
    void setId(String id);

    String getFirstName();
    void setFirstName(String firstName);

    void setLastName(String lastName);
    String getLastName();

    void setEmail(String email);
    String getEmail();

    String getPassword();
    void setPassword(String string);
}
