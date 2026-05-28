package edu.epicode.ticketing.repositories;

import edu.epicode.ticketing.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import edu.epicode.ticketing.entities.User;

@Repository
public interface TicketsRepository extends JpaRepository<Ticket, Long> {

    @Modifying
    @Query("UPDATE Ticket t SET t.user = null, t.userDeleted = true WHERE t.user = :user")
    void detachUserFromTickets(@Param("user") User user);

    org.springframework.data.domain.Page<Ticket> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description, org.springframework.data.domain.Pageable pageable);
}
