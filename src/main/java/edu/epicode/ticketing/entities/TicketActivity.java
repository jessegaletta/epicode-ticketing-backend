package edu.epicode.ticketing.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "ticket_activities")
public class TicketActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    @JsonIgnore
    private Ticket ticket;

    @Column(nullable = false)
    private boolean userDeleted = false;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isStatusChange = false;

    public TicketActivity() {
    }

    public TicketActivity(String text, User user, Ticket ticket) {
        this.text = text;
        this.user = user;
        this.ticket = ticket;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public boolean isUserDeleted() {
        return userDeleted;
    }

    public void setUserDeleted(boolean userDeleted) {
        this.userDeleted = userDeleted;
    }

    public boolean isStatusChange() {
        return isStatusChange;
    }

    public void setStatusChange(boolean statusChange) {
        isStatusChange = statusChange;
    }

    @JsonProperty("authorBachelorDescription")
    public String getAuthorBachelorDescription() {
        if (this.user != null && this.user.getBachelor() != null) {
            return this.user.getBachelor().getDescription();
        }
        return "";
    }
}
