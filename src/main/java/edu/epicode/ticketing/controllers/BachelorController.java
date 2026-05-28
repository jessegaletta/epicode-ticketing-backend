package edu.epicode.ticketing.controllers;

import edu.epicode.ticketing.entities.Bachelor;
import edu.epicode.ticketing.exceptions.ValidationException;
import edu.epicode.ticketing.payloads.bachelors.BachelorDTO;
import edu.epicode.ticketing.services.BachelorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bachelors")
public class BachelorController {

    @Autowired
    private BachelorService bachelorService;

    @GetMapping
    public Page<Bachelor> getAllBachelors(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "id") String sortBy,
                                          @RequestParam(defaultValue = "ASC") String sortDir,
                                          @RequestParam(required = false) String search) {
        return bachelorService.getBachelors(page, size, sortBy, sortDir, search);
    }

    @GetMapping("/{id}")
    public Bachelor getBachelorById(@PathVariable Long id) {
        return bachelorService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'FACULTY')")
    @ResponseStatus(HttpStatus.CREATED)
    public Bachelor saveBachelor(@RequestBody @Validated BachelorDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new ValidationException(validation.getAllErrors().stream().map(org.springframework.context.support.DefaultMessageSourceResolvable::getDefaultMessage).toList());
        }
        return bachelorService.save(body);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'FACULTY')")
    public Bachelor updateBachelor(@PathVariable Long id, @RequestBody @Validated BachelorDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new ValidationException(validation.getAllErrors().stream().map(org.springframework.context.support.DefaultMessageSourceResolvable::getDefaultMessage).toList());
        }
        return bachelorService.update(id, body);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'FACULTY')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBachelor(@PathVariable Long id) {
        bachelorService.delete(id);
    }
}
