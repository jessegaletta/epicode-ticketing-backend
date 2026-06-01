package edu.epicode.ticketing.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "tickets")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "category", discriminatorType = DiscriminatorType.STRING)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(updatable = false)
    private Instant createdAt;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.UNASSIGNED;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private boolean userDeleted = false;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<TicketActivity> activities;

    @Column(name = "category", insertable = false, updatable = false)
    private String category;

    public Ticket() {
    }

    public Ticket(String title, String description, User user) {
        this.title = title;
        this.description = description;
        this.status = TicketStatus.UNASSIGNED;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
        this.lastUpdate = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdate = Instant.now();
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isUserDeleted() {
        return userDeleted;
    }

    public void setUserDeleted(boolean userDeleted) {
        this.userDeleted = userDeleted;
    }

    public List<TicketActivity> getActivities() {
        return activities;
    }

    public void setActivities(List<TicketActivity> activities) {
        this.activities = activities;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", status=" + status +
                ", user=" + (user != null ? user.getId() : "null") +
                '}';
    }

    @JsonProperty("authorBachelorDescription")
    public String getAuthorBachelorDescription() {
        if (this.user != null && this.user.getBachelor() != null) {
            return this.user.getBachelor().getDescription();
        }
        return "";
    }
}
