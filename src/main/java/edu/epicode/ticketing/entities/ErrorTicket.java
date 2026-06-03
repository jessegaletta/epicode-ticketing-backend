package edu.epicode.ticketing.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// Subclass of Ticket using JPA JOINED inheritance: only the extra fields specific to this
// type are stored in the "error_tickets" table, joined to "tickets" by the primary key.
// @DiscriminatorValue sets the string written to the "category" column in the parent table.
@Entity
@Table(name = "error_tickets")
@DiscriminatorValue("ERROR")
public class ErrorTicket extends Ticket {

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private String moduleName;

    private String lessonName;

    public ErrorTicket() {
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }
}
