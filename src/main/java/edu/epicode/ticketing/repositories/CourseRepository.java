package edu.epicode.ticketing.repositories;

import edu.epicode.ticketing.entities.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);
    Page<Course> findByBachelors_Id(Long bachelorId, Pageable pageable);
    Page<Course> findByDescriptionContainingIgnoreCaseAndBachelors_Id(String description, Long bachelorId, Pageable pageable);
    List<Course> findByBachelors_Id(Long bachelorId, Sort sort);
}
