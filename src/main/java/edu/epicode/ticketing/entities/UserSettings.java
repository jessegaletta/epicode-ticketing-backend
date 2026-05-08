package edu.epicode.ticketing.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "user_settings")
public class UserSettings {
    @Id
    @GeneratedValue
    private UUID id;

    private boolean emailNotifications;
    private boolean darkMode;
    private String timezone;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UserSettings(){}

    public UserSettings(User user){
        this.user = user;
        this.darkMode = false;
        this.emailNotifications = false;
        this.timezone = "Europe/Rome";
    }

    public UUID getId() {
        return id;
    }

    public boolean isEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "id=" + id +
                ", emailNotifications=" + emailNotifications +
                ", darkMode=" + darkMode +
                ", timezone='" + timezone + '\'' +
                ", user=" + user +
                '}';
    }
}
