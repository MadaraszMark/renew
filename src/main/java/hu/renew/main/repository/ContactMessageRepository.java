package hu.renew.main.repository;

import hu.renew.main.model.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Integer> {
    boolean existsByEmailAndMessage(String email, String message);
}

