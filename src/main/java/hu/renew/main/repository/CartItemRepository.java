package hu.renew.main.repository;

import hu.renew.main.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findBySessionId(String sessionId);
    CartItem findBySessionIdAndProductId(String sessionId, Integer productId);
    void deleteBySessionId(String sessionId);
}

