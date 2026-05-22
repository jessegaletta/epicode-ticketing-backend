package edu.epicode.ticketing.services;

import edu.epicode.ticketing.entities.Role;
import edu.epicode.ticketing.entities.Ticket;
import edu.epicode.ticketing.entities.User;
import edu.epicode.ticketing.exceptions.NotFoundException;
import edu.epicode.ticketing.exceptions.UnauthorizedException;
import edu.epicode.ticketing.payloads.tickets.NewTicketDTO;
import edu.epicode.ticketing.payloads.tickets.UpdateTicketDTO;
import edu.epicode.ticketing.repositories.TicketsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TicketsService {

    @Autowired
    private TicketsRepository ticketsRepository;

    public Ticket save(NewTicketDTO body, User currentUser) {
        User author = currentUser;
        
        // If the user chooses to be anonymous, we don't link the ticket to them.
        if (body.isAnonymous()) {
            author = null;
        }

        Ticket newTicket = new Ticket(body.title(), body.description(), author);
        return ticketsRepository.save(newTicket);
    }

    public Page<Ticket> findAll(int page, int size, String sortBy, String sortDir) {
        if (size > 100) size = 100;
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ticketsRepository.findAll(pageable);
    }

    public Ticket findById(Long id) {
        return ticketsRepository.findById(id).orElseThrow(() -> new NotFoundException(String.valueOf(id)));
    }

    public Ticket findByIdAndUpdate(Long id, UpdateTicketDTO body, User currentUser) {
        Ticket ticket = this.findById(id);

        checkEditPermission(ticket, currentUser);

        ticket.setTitle(body.title());
        ticket.setDescription(body.description());
        ticket.setStatus(body.status());

        return ticketsRepository.save(ticket);
    }

    public void findByIdAndDelete(Long id, User currentUser) {
        Ticket ticket = this.findById(id);

        checkEditPermission(ticket, currentUser);

        ticketsRepository.delete(ticket);
    }

    private void checkEditPermission(Ticket ticket, User currentUser) {
        if (currentUser == null) {
            throw new UnauthorizedException("You must be logged in to modify this ticket.");
        }
        
        boolean isAdmin = currentUser.getRole() == Role.ADMIN || currentUser.getRole() == Role.SUPERADMIN;
        
        if (ticket.getUser() == null) {
            // Anonymous ticket: only admins can edit
            if (!isAdmin) {
                throw new UnauthorizedException("Anonymous tickets can only be modified by administrators.");
            }
        } else {
            // User ticket: owner or admin can edit
            boolean isOwner = ticket.getUser().getId().equals(currentUser.getId());
            if (!isOwner && !isAdmin) {
                throw new UnauthorizedException("You are not authorized to modify this ticket.");
            }
        }
    }
}
