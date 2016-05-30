package com.valapay.vala.common;

/**
 * Created by noam on 30/03/2016.
 */
public class UserNameQueryResult {

    private String userId;
    private String name;

    public UserNameQueryResult() {
    }

    public String getUserId() {
        return userId;
    }

    public UserNameQueryResult setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserNameQueryResult setName(String name) {
        this.name = name;
        return this;
    }
}