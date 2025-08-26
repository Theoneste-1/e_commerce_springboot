package com.example.e_commerce.controllers.cart;


import com.example.e_commerce.dto.cart.CartCreateRequest;
import com.example.e_commerce.dto.cart.CartItemCreateRequest;
import com.example.e_commerce.dto.cart.CartResponse;
import com.example.e_commerce.dto.cart.CartUpdateRequest;
import com.example.e_commerce.services.cart.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CartResponse> createCart(@Valid @RequestBody CartCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.createCart(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartResponse> getCartById(@PathVariable String id) {
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<CartResponse> addCartItem(@PathVariable String id, @Valid @RequestBody CartItemCreateRequest request) {
        return ResponseEntity.ok(cartService.addCartItem(id, request));
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<CartResponse> removeCartItem(@PathVariable String id, @PathVariable String itemId) {
        return ResponseEntity.ok(cartService.removeCartItem(id, itemId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartResponse> updateCart(@PathVariable String id, @Valid @RequestBody CartUpdateRequest request) {
        return ResponseEntity.ok(cartService.updateCart(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable String id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }
}