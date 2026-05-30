package edu.epicode.ticketing.services;

import edu.epicode.ticketing.entities.Role;
import edu.epicode.ticketing.entities.Ticket;
import edu.epicode.ticketing.entities.TicketActivity;
import edu.epicode.ticketing.entities.User;
import edu.epicode.ticketing.exceptions.NotFoundException;
import edu.epicode.ticketing.exceptions.UnauthorizedException;
import edu.epicode.ticketing.payloads.tickets.NewActivityDTO;
import edu.epicode.ticketing.payloads.tickets.UpdateActivityDTO;
import edu.epicode.ticketing.repositories.TicketActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketActivityService {

    @Autowired
    private TicketActivityRepository activityRepository;

    @Autowired
    private TicketsService ticketsService;

    public TicketActivity save(Long ticketId, NewActivityDTO body, User currentUser) {
        Ticket ticket = ticketsService.findById(ticketId);
        
        User author = currentUser;
        if (body.isAnonymous()) {
            author = null;
        }

        TicketActivity activity = new TicketActivity(body.text(), author, ticket);
        return activityRepository.save(activity);
    }

    public List<TicketActivity> findByTicketId(Long ticketId) {
        // Ensure ticket exists
        ticketsService.findById(ticketId);
        return activityRepository.findByTicketIdOrderByCreatedAtDesc(ticketId);
    }

    public TicketActivity findById(Long id) {
        return activityRepository.findById(id).orElseThrow(() -> new NotFoundException(String.valueOf(id)));
    }

    public TicketActivity findByIdAndUpdate(Long id, UpdateActivityDTO body, User currentUser) {
        TicketActivity activity = this.findById(id);

        checkEditPermission(activity, currentUser);

        activity.setText(body.text());

        return activityRepository.save(activity);
    }

    public void findByIdAndDelete(Long id, User currentUser) {
        TicketActivity activity = this.findById(id);

        checkEditPermission(activity, currentUser);

        activityRepository.delete(activity);
    }

    private void checkEditPermission(TicketActivity activity, User currentUser) {
        if (currentUser == null) {
            throw new UnauthorizedException("You must be logged in to modify this activity.");
        }
        
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        
        if (activity.getUser() == null) {
            // Anonymous activity: only admins can edit
            if (!isAdmin) {
                throw new UnauthorizedException("Anonymous activities can only be modified by administrators.");
            }
        } else {
            // User activity: owner or admin can edit
            boolean isOwner = activity.getUser().getId().equals(currentUser.getId());
            if (!isOwner && !isAdmin) {
                throw new UnauthorizedException("You are not authorized to modify this activity.");
            }
        }
    }
}
