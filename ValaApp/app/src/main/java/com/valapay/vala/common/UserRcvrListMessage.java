package com.valapay.vala.common;

/**
 * Created by noam on 10/04/2016.
 */
public class UserRcvrListMessage extends Message {

    public enum Action {
        ADD, DELETE
    }

    private String userId;
    private String rcvrId;
    private Action action;

    public UserRcvrListMessage() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRcvrId() {
        return rcvrId;
    }

    public void setRcvrId(String rcvrId) {
        this.rcvrId = rcvrId;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "UserRcvrListMessage{" +
                "userId='" + userId + '\'' +
                ", rcvrId='" + rcvrId + '\'' +
                ", action=" + action +
                '}';
    }
}