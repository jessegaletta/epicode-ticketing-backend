package edu.epicode.ticketing.repositories;

import edu.epicode.ticketing.entities.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);
}
