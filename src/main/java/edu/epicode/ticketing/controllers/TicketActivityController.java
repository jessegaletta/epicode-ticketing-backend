package edu.epicode.ticketing.controllers;

import edu.epicode.ticketing.entities.TicketActivity;
import edu.epicode.ticketing.entities.User;
import edu.epicode.ticketing.payloads.tickets.NewActivityDTO;
import edu.epicode.ticketing.payloads.tickets.UpdateActivityDTO;
import edu.epicode.ticketing.services.TicketActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TicketActivityController {

    @Autowired
    private TicketActivityService activityService;

    @GetMapping("/tickets/{ticketId}/activities")
    public List<TicketActivity> getTicketActivities(@PathVariable Long ticketId) {
        return activityService.findByTicketId(ticketId);
    }

    @PostMapping("/tickets/{ticketId}/activities")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketActivity createActivity(
            @PathVariable Long ticketId,
            @Validated @RequestBody NewActivityDTO body,
            @AuthenticationPrincipal User currentUser
    ) {
        return activityService.save(ticketId, body, currentUser);
    }

    @PutMapping("/activities/{id}")
    @PreAuthorize("isAuthenticated()")
    public TicketActivity updateActivity(
            @PathVariable Long id,
            @Validated @RequestBody UpdateActivityDTO body,
            @AuthenticationPrincipal User currentUser
    ) {
        return activityService.findByIdAndUpdate(id, body, currentUser);
    }

    @DeleteMapping("/activities/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void deleteActivity(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        activityService.findByIdAndDelete(id, currentUser);
    }
}
