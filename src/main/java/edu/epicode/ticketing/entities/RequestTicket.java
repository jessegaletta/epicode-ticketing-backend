package edu.epicode.ticketing.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "request_tickets")
@DiscriminatorValue("REQUEST")
public class RequestTicket extends Ticket {

    @Enumerated(EnumType.STRING)
    private RequestType requestType;

    public RequestTicket() {
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }
}
