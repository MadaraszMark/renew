package hu.renew.main.service;

import hu.renew.main.model.CartItem;
import hu.renew.main.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository repo;

    public List<CartItem> getCart(String sessionId) {
        return repo.findBySessionId(sessionId);
    }

    public CartItem addItem(CartItem item) {
        return repo.save(item);
    }

    public void clearCart(String sessionId) {
        repo.findBySessionId(sessionId).forEach(repo::delete);
    }

    public void removeItem(Integer id) {
        repo.deleteById(id);
    }
}

