package edu.epicode.ticketing.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "user_settings")
public class UserSettings {
    @Id
    @GeneratedValue
    private UUID id;


    private boolean darkMode;
    private String timezone;
    private String dateFormat;
    private String timeFormat;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UserSettings(){}

    public UserSettings(User user){
        this.user = user;
        this.darkMode = true;
        this.timezone = "Europe/Amsterdam";
        this.dateFormat = "DD/MM/YYYY";
        this.timeFormat = "24h";
    }

    public UUID getId() {
        return id;
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

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "id=" + id +
                ", darkMode=" + darkMode +
                ", timezone='" + timezone + '\'' +
                ", dateFormat='" + dateFormat + '\'' +
                ", timeFormat='" + timeFormat + '\'' +
                ", user=" + user +
                '}';
    }
}
