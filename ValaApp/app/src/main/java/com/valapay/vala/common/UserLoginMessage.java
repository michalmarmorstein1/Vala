package com.valapay.vala.common;

/**
 * Created by noam on 2/8/16.
 */
public class UserLoginMessage extends Message {

    private String email;
    private String password;
    private String token;
    private String userId;

    public UserLoginMessage(String email, String password, String token) {
        super();
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public UserLoginMessage(String email, String password) {
        super();
        this.email = email;
        this.password = password;
    }

    public UserLoginMessage() {
        super();
    }

    public String getEmail() {
        return email;
    }

    public UserLoginMessage setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserLoginMessage setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}