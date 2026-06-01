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

    @Query("SELECT t FROM Ticket t WHERE " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:category IS NULL OR t.category = :category) AND " +
           "(:search IS NULL OR :search = '' OR LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:onlyOpen = false OR (t.status != edu.epicode.ticketing.entities.TicketStatus.RESOLVED AND t.status != edu.epicode.ticketing.entities.TicketStatus.REJECTED))")
    org.springframework.data.domain.Page<Ticket> findByFilters(
            @Param("status") edu.epicode.ticketing.entities.TicketStatus status,
            @Param("category") String category,
            @Param("search") String search,
            @Param("onlyOpen") boolean onlyOpen,
            org.springframework.data.domain.Pageable pageable);
}
