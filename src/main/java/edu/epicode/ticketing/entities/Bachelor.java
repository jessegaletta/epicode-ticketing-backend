package edu.epicode.ticketing.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "bachelors")
public class Bachelor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "bachelor")
    @JsonIgnore
    private List<User> users;

    @ManyToMany(mappedBy = "bachelors")
    @JsonIgnore
    private Set<Course> courses = new HashSet<>();

    public Bachelor() {
    }

    public Bachelor(String description) {
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "Bachelor{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
