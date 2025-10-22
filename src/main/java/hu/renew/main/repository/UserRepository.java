package hu.renew.main.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hu.renew.main.model.ContactMessage;
import hu.renew.main.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    Page<ContactMessage> findByEmail(String email, Pageable pageable);
    boolean existsByEmail(String email);
    Page<User> findAll(Pageable pageable);
}
