package edu.epicode.ticketing.controllers;

import edu.epicode.ticketing.entities.Ticket;
import edu.epicode.ticketing.entities.User;
import edu.epicode.ticketing.payloads.tickets.NewTicketDTO;
import edu.epicode.ticketing.payloads.tickets.UpdateTicketDTO;
import edu.epicode.ticketing.payloads.tickets.ChangeStatusDTO;
import edu.epicode.ticketing.services.TicketsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
public class TicketsController {

    @Autowired
    private TicketsService ticketsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ticket createTicket(
            @Validated @RequestBody NewTicketDTO body,
            @AuthenticationPrincipal User currentUser
    ) {
        return ticketsService.save(body, currentUser);
    }

    @GetMapping
    public Page<Ticket> getTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            @RequestParam(required = false) String search
    ) {
        return ticketsService.findAll(page, size, sortBy, sortDir, search);
    }

    @GetMapping("/{id}")
    public Ticket getTicketById(@PathVariable Long id) {
        return ticketsService.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Ticket updateTicket(
            @PathVariable Long id,
            @Validated @RequestBody UpdateTicketDTO body,
            @AuthenticationPrincipal User currentUser
    ) {
        return ticketsService.findByIdAndUpdate(id, body, currentUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void deleteTicket(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        ticketsService.findByIdAndDelete(id, currentUser);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'FACULTY')")
    public Ticket changeStatus(
            @PathVariable Long id,
            @Validated @RequestBody ChangeStatusDTO body,
            @AuthenticationPrincipal User currentUser
    ) {
        return ticketsService.changeStatus(id, body, currentUser);
    }
}
