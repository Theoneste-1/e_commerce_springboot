package com.example.e_commerce.controllers.wishlist;


import com.example.e_commerce.dto.wishlist.WishlistCreateRequest;
import com.example.e_commerce.dto.wishlist.WishlistItemCreateRequest;
import com.example.e_commerce.dto.wishlist.WishlistResponse;
import com.example.e_commerce.dto.wishlist.WishlistUpdateRequest;
import com.example.e_commerce.services.wishlist.WishlistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlists")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping
    public ResponseEntity<WishlistResponse> createWishlist(@Valid @RequestBody WishlistCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(wishlistService.createWishlist(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WishlistResponse> getWishlistById(@PathVariable String id) {
        return ResponseEntity.ok(wishlistService.getWishlistById(id));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<WishlistResponse> addWishlistItem(@PathVariable String id, @Valid @RequestBody WishlistItemCreateRequest request) {
        return ResponseEntity.ok(wishlistService.addWishlistItem(id, request));
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<WishlistResponse> removeWishlistItem(@PathVariable String id, @PathVariable String itemId) {
        return ResponseEntity.ok(wishlistService.removeWishlistItem(id, itemId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WishlistResponse> updateWishlist(@PathVariable String id, @Valid @RequestBody WishlistUpdateRequest request) {
        return ResponseEntity.ok(wishlistService.updateWishlist(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWishlist(@PathVariable String id) {
        wishlistService.deleteWishlist(id);
        return ResponseEntity.noContent().build();
    }
}