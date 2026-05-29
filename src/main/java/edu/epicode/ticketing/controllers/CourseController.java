package edu.epicode.ticketing.controllers;

import edu.epicode.ticketing.entities.Course;
import edu.epicode.ticketing.exceptions.ValidationException;
import edu.epicode.ticketing.payloads.courses.CourseDTO;
import edu.epicode.ticketing.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public Page<Course> getAllCourses(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "id") String sortBy,
                                      @RequestParam(defaultValue = "ASC") String sortDir,
                                      @RequestParam(required = false) String search) {
        return courseService.getCourses(page, size, sortBy, sortDir, search);
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable Long id) {
        return courseService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'FACULTY')")
    @ResponseStatus(HttpStatus.CREATED)
    public Course saveCourse(@RequestBody @Validated CourseDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new ValidationException(validation.getAllErrors().stream().map(org.springframework.context.support.DefaultMessageSourceResolvable::getDefaultMessage).toList());
        }
        return courseService.save(body);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'FACULTY')")
    public Course updateCourse(@PathVariable Long id, @RequestBody @Validated CourseDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new ValidationException(validation.getAllErrors().stream().map(org.springframework.context.support.DefaultMessageSourceResolvable::getDefaultMessage).toList());
        }
        return courseService.update(id, body);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'FACULTY')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
    }
}
