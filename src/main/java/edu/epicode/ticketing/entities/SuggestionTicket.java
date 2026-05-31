package edu.epicode.ticketing.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "suggestion_tickets")
@DiscriminatorValue("SUGGESTION")
public class SuggestionTicket extends Ticket {

    @Column(columnDefinition = "TEXT")
    private String expectedBenefit;

    public SuggestionTicket() {
    }

    public String getExpectedBenefit() {
        return expectedBenefit;
    }

    public void setExpectedBenefit(String expectedBenefit) {
        this.expectedBenefit = expectedBenefit;
    }
}
