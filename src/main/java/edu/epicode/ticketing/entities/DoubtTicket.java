package edu.epicode.ticketing.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// Subclass of Ticket using JPA JOINED inheritance. Extra fields are in the "doubt_tickets" table.
// @DiscriminatorValue sets the string written to the "category" column in the parent table.
@Entity
@Table(name = "doubt_tickets")
@DiscriminatorValue("DOUBT")
public class DoubtTicket extends Ticket {

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private boolean isFaqCandidate;

    public DoubtTicket() {
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public boolean isFaqCandidate() {
        return isFaqCandidate;
    }

    public void setFaqCandidate(boolean faqCandidate) {
        isFaqCandidate = faqCandidate;
    }
}
