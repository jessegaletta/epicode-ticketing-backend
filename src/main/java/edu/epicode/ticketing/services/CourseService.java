package edu.epicode.ticketing.services;

import edu.epicode.ticketing.entities.Bachelor;
import edu.epicode.ticketing.entities.Course;
import edu.epicode.ticketing.entities.User;
import edu.epicode.ticketing.exceptions.NotFoundException;
import edu.epicode.ticketing.exceptions.ValidationException;
import edu.epicode.ticketing.payloads.courses.CourseDTO;
import edu.epicode.ticketing.repositories.BachelorRepository;
import edu.epicode.ticketing.repositories.CourseRepository;
import edu.epicode.ticketing.repositories.TicketsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private BachelorRepository bachelorRepository;

    @Autowired
    private TicketsRepository ticketsRepository;

    public Page<Course> getCourses(int page, int size, String sortBy, String sortDir, String search, User currentUser) {
        if (size > 100) size = 100;
        Sort.Order order = new Sort.Order(Sort.Direction.fromString(sortDir), sortBy);
        if ("description".equals(sortBy)) {
            order = order.ignoreCase();
        }
        Sort sort = Sort.by(order);
        Pageable pageable = PageRequest.of(page, size, sort);

        Long bachelorId = (currentUser != null && currentUser.getBachelor() != null) 
            ? currentUser.getBachelor().getId() 
            : null;

        if (search != null && !search.trim().isEmpty()) {
            if (bachelorId != null) {
                return courseRepository.findByDescriptionContainingIgnoreCaseAndBachelors_Id(search, bachelorId, pageable);
            }
            return courseRepository.findByDescriptionContainingIgnoreCase(search, pageable);
        }

        if (bachelorId != null) {
            return courseRepository.findByBachelors_Id(bachelorId, pageable);
        }
        return courseRepository.findAll(pageable);
    }

    public List<Course> getAllCoursesList(User currentUser) {
        Long bachelorId = (currentUser != null && currentUser.getBachelor() != null) 
            ? currentUser.getBachelor().getId() 
            : null;

        Sort sort = Sort.by(Sort.Order.asc("description").ignoreCase());
        if (bachelorId != null) {
            return courseRepository.findByBachelors_Id(bachelorId, sort);
        }
        return courseRepository.findAll(sort);
    }

    public Course save(CourseDTO body) {
        Course course = new Course(body.description());
        if (body.bachelorIds() != null && !body.bachelorIds().isEmpty()) {
            List<Bachelor> bachelorsList = bachelorRepository.findAllById(body.bachelorIds());
            Set<Bachelor> bachelors = new HashSet<>(bachelorsList);
            course.setBachelors(bachelors);
            
            // Manage bidirectional relation
            for (Bachelor b : bachelors) {
                b.getCourses().add(course);
            }
        }
        return courseRepository.save(course);
    }

    public Course findById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Course with id " + id + " not found"));
    }

    public Course update(Long id, CourseDTO body) {
        Course found = this.findById(id);
        found.setDescription(body.description());
        
        if (body.bachelorIds() != null) {
            List<Bachelor> bachelorsList = bachelorRepository.findAllById(body.bachelorIds());
            Set<Bachelor> newBachelors = new HashSet<>(bachelorsList);
            
            // Remove course from old bachelors
            for (Bachelor oldB : found.getBachelors()) {
                oldB.getCourses().remove(found);
            }
            
            // Add course to new bachelors
            for (Bachelor newB : newBachelors) {
                newB.getCourses().add(found);
            }
            
            found.setBachelors(newBachelors);
        }
        
        return courseRepository.save(found);
    }

    public void delete(Long id) {
        Course found = this.findById(id);
        
        if (ticketsRepository.existsErrorTicketByCourseId(id) || ticketsRepository.existsDoubtTicketByCourseId(id)) {
            throw new ValidationException("Cannot delete this course because there are tickets associated with it.");
        }

        // Remove course from all bachelors before deleting
        for (Bachelor b : found.getBachelors()) {
            b.getCourses().remove(found);
        }
        courseRepository.delete(found);
    }
}
