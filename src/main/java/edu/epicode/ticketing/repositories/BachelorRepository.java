package edu.epicode.ticketing.repositories;

import edu.epicode.ticketing.entities.Bachelor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BachelorRepository extends JpaRepository<Bachelor, Long> {
}
