package edu.epicode.ticketing.services;

import edu.epicode.ticketing.entities.Bachelor;
import edu.epicode.ticketing.exceptions.ValidationException;
import edu.epicode.ticketing.exceptions.NotFoundException;
import edu.epicode.ticketing.payloads.bachelors.BachelorDTO;
import edu.epicode.ticketing.repositories.BachelorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BachelorService {

    @Autowired
    private BachelorRepository bachelorRepository;

    public Page<Bachelor> getBachelors(int page, int size, String sortBy, String sortDir, String search) {
        if (size > 100) size = 100;
        Sort.Order order = new Sort.Order(Sort.Direction.fromString(sortDir), sortBy);
        if ("description".equals(sortBy)) {
            order = order.ignoreCase();
        }
        Sort sort = Sort.by(order);
        Pageable pageable = PageRequest.of(page, size, sort);
        if (search != null && !search.trim().isEmpty()) {
            return bachelorRepository.findByDescriptionContainingIgnoreCase(search, pageable);
        }
        return bachelorRepository.findAll(pageable);
    }

    public Bachelor save(BachelorDTO body) {
        Bachelor bachelor = new Bachelor(body.description());
        return bachelorRepository.save(bachelor);
    }

    public Bachelor findById(Long id) {
        return bachelorRepository.findById(id).orElseThrow(() -> new NotFoundException("Bachelor with id " + id + " not found"));
    }

    public Bachelor update(Long id, BachelorDTO body) {
        Bachelor found = this.findById(id);
        found.setDescription(body.description());
        return bachelorRepository.save(found);
    }

    public void delete(Long id) {
        Bachelor found = this.findById(id);
        if (found.getUsers() != null && !found.getUsers().isEmpty()) {
            throw new ValidationException("Cannot delete this bachelor because there are users associated with it.");
        }
        if (found.getCourses() != null && !found.getCourses().isEmpty()) {
            throw new ValidationException("Cannot delete this bachelor because there are courses associated with it.");
        }
        bachelorRepository.delete(found);
    }
}
