package com.fds.softlog.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users_data")
public class UserData {

    private String id;
    private User user;
    private int createOperations;
    private int readOperations;
    private int updateOperations;
    private int deleteOperations;

    public String getId() {
        return id;
    }

    public UserData(User user) {
        this.user = user;
        this.id = user.getId();
        this.createOperations = 0;
        this.readOperations = 0;
        this.updateOperations = 0;
        this.deleteOperations = 0;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCreateOperations() {
        return createOperations;
    }

    public void setCreateOperations(int createOperations) {
        this.createOperations = createOperations;
    }

    public int getReadOperations() {
        return readOperations;
    }

    public void setReadOperations(int readOperations) {
        this.readOperations = readOperations;
    }

    public int getUpdateOperations() {
        return updateOperations;
    }

    public void setUpdateOperations(int updateOperations) {
        this.updateOperations = updateOperations;
    }

    public int getDeleteOperations() {
        return deleteOperations;
    }

    public void setDeleteOperations(int deleteOperations) {
        this.deleteOperations = deleteOperations;
    }
}
