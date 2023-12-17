package com.fds.softlog.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users_data")
public class UserData {

    private String id;
    private User user;
    private int readOperations;
    private int writeOperations;

    public String getId() {
        return id;
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

    public int getReadOperations() {
        return readOperations;
    }

    public void setReadOperations(int readOperations) {
        this.readOperations = readOperations;
    }

    public int getWriteOperations() {
        return writeOperations;
    }

    public void setWriteOperations(int writeOperations) {
        this.writeOperations = writeOperations;
    }
}
