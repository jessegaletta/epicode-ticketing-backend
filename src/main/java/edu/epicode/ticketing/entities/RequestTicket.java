package edu.epicode.ticketing.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

// Subclass of Ticket using JPA JOINED inheritance. Extra fields are in the "request_tickets" table.
// @DiscriminatorValue sets the string written to the "category" column in the parent table.
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
