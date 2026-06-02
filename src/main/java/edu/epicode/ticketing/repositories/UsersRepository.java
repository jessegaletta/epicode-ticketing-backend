package edu.epicode.ticketing.repositories;

import edu.epicode.ticketing.entities.Role;
import edu.epicode.ticketing.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<User, UUID> {

    // =============== JPQL QUERIES ===============

    @Query("SELECT u FROM User u WHERE u.firstName = : name")
    List<User> findUsersByName(String name);

    // =============== DERIVED QUERIES ===============
    Optional<User> findByEmail(String email); // SELECT u FROM User u WHERE u.email = :email;

    boolean existsByEmail(String email);

    Page<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String firstName, String lastName, String email, Pageable pageable);

    List<User> findByFirstNameAndLastName(String firstName, String lastName);

    Long countByRole(Role role);

    // =============== NATIVE QUERIES ===============
    @Query(value = "SELECT * users WHERE name = :name", nativeQuery = true)
    List<User> searchUsersByName(String name);

    List<User> findByRole(Role role);
}