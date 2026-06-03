package edu.epicode.ticketing.repositories;

import edu.epicode.ticketing.entities.Ticket;
import edu.epicode.ticketing.entities.TicketStatus;
import edu.epicode.ticketing.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketsRepository extends JpaRepository<Ticket, Long> {

    // @Modifying is required for write queries (UPDATE/DELETE).
    // Without it, Spring Data throws an exception expecting a SELECT.
    @Modifying
    @Query("UPDATE Ticket t SET t.user = null, t.userDeleted = true WHERE t.user = :user")
    void detachUserFromTickets(@Param("user") User user);

    Page<Ticket> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description, Pageable pageable);

    // Each filter parameter is optional: passing null skips that condition.
    // A separate countQuery is required because LEFT JOIN FETCH is not allowed in count queries.
    @Query(value = "SELECT t FROM Ticket t LEFT JOIN FETCH t.user WHERE " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:category IS NULL OR t.category = :category) AND " +
           "(:search IS NULL OR :search = '' OR LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:onlyOpen = false OR (t.status != 'RESOLVED' AND t.status != 'REJECTED'))",
           countQuery = "SELECT COUNT(t) FROM Ticket t WHERE " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:category IS NULL OR t.category = :category) AND " +
           "(:search IS NULL OR :search = '' OR LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:onlyOpen = false OR (t.status != 'RESOLVED' AND t.status != 'REJECTED'))")
    Page<Ticket> findByFilters(
            @Param("status") TicketStatus status,
            @Param("category") String category,
            @Param("search") String search,
            @Param("onlyOpen") boolean onlyOpen,
            Pageable pageable);
}
