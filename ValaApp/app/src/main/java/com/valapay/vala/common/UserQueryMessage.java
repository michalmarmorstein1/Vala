package com.valapay.vala.common;

import java.util.List;

/**
 * Created by noam on 15/03/2016.
 */
public class UserQueryMessage extends Message {

    public boolean done() {
        return result.getCountry() != null && result.getFirstName() != null
                && result.getLastName() != null && result.getPhoneNumber() != null;
    }

    public enum QueryType {
        EMAIL, ID, NAME, RCVRS
    }

    private String query;
    private QueryType queryType;
    private UserSignupMessage result;
    private List<UserNameQueryResult> names;

    public UserQueryMessage() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }

    public UserSignupMessage getResult() {
        return result;
    }

    public void setResult(UserSignupMessage result) {
        this.result = result;
    }

    public List<UserNameQueryResult> getNames() {
        return names;
    }

    public void setNames(List<UserNameQueryResult> names) {
        this.names = names;
    }

    @Override
    public String toString() {
        return "UserQueryMessage{" +
                "query='" + query + '\'' +
                ", queryType=" + queryType +
                ", result=" + result +
                ", names=" + names +
                '}';
    }
}
