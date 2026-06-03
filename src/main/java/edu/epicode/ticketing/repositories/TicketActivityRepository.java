package edu.epicode.ticketing.repositories;

import edu.epicode.ticketing.entities.TicketActivity;
import edu.epicode.ticketing.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketActivityRepository extends JpaRepository<TicketActivity, Long> {

    List<TicketActivity> findByTicketIdOrderByCreatedAtDesc(Long ticketId);

    // @Modifying + @Query is used here because Spring Data cannot auto-generate an UPDATE
    // query from a method name alone.
    @Modifying
    @Query("UPDATE TicketActivity a SET a.user = null, a.userDeleted = true WHERE a.user = :user")
    void detachUserFromActivities(@Param("user") User user);
}
