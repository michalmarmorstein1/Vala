package com.valapay.vala.common;

import java.util.UUID;

/**
 *
 * A super class for data sent between the service, but not on the event bus.
 *
 * Any object that is used in the services network should extend this class.
 * This class defines the correlation id, this id ties a user action across the
 * whole system
 *
 * Created by noam on 2/14/16.
 *
 */
public abstract class Message {

    public enum Status {
        FAIL, SUCCESS
    }

    private String correlationId;

    private Status status = Status.SUCCESS;

    public Message() {
        this.correlationId = UUID.randomUUID().toString();
    }

    public Message(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
