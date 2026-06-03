package edu.epicode.ticketing.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @ManyToMany
    @JoinTable(name = "courses_bachelors",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "bachelor_id"))
    private Set<Bachelor> bachelors = new HashSet<>();

    public Course() {
    }

    public Course(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Bachelor> getBachelors() {
        return bachelors;
    }

    public void setBachelors(Set<Bachelor> bachelors) {
        this.bachelors = bachelors;
    }
    
    // Both sides of the ManyToMany relationship must be updated manually in memory.
    // JPA does not automatically sync the inverse side within the same session.
    public void addBachelor(Bachelor bachelor) {
        this.bachelors.add(bachelor);
        bachelor.getCourses().add(this);
    }

    public void removeBachelor(Bachelor bachelor) {
        this.bachelors.remove(bachelor);
        bachelor.getCourses().remove(this);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
