package hu.renew.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hu.renew.main.model.ContactMessage;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Integer> {
    boolean existsByEmailAndMessage(String email, String message);
    Page<ContactMessage> findByEmail(String email, Pageable pageable);
}

