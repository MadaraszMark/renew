package hu.renew.main.controller;

import hu.renew.main.model.CartItem;
import hu.renew.main.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService service;

    @GetMapping("/session/{sessionId}")
    public List<CartItem> getCart(@PathVariable String sessionId) {
        return service.getCart(sessionId);
    }

    @PostMapping("/add")
    public ResponseEntity<CartItem> addItem(@RequestBody CartItem item) {
        CartItem saved = service.addItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Integer id) {
        service.removeItem(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<Void> clearCart(@PathVariable String sessionId) {
        service.clearCart(sessionId);
        return ResponseEntity.noContent().build();
    }
}


