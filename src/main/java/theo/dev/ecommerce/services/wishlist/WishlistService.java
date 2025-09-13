package theo.dev.ecommerce.services.wishlist;

import theo.dev.ecommerce.dto.wishlist.*;
import theo.dev.ecommerce.exceptions.ResourceNotFoundException;
import theo.dev.ecommerce.models.auth.User;
import theo.dev.ecommerce.models.product.Product;
import theo.dev.ecommerce.models.wishlist.Wishlist;
import theo.dev.ecommerce.models.wishlist.WishlistItem;
import theo.dev.ecommerce.repositories.product.ProductRepository;
import theo.dev.ecommerce.repositories.wishlist.WishlistRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {
    private static final Logger logger = LoggerFactory.getLogger(WishlistService.class);
    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public WishlistResponse createWishlist(WishlistCreateRequest request) {
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(new User());
        wishlist = wishlistRepository.save(wishlist);
        logger.info("Created wishlist with ID: {}", wishlist.getId());
        return mapToResponse(wishlist);
    }

    @Transactional
    public WishlistResponse addWishlistItem(String wishlistId, WishlistItemCreateRequest request) {
        Wishlist wishlist = wishlistRepository.findByIdNative(wishlistId);
        if (wishlist == null)
            throw new ResourceNotFoundException("Wishlist not found: " + wishlistId);
        Product product = productRepository.findByIdNative(request.productId());
        if (product == null)
            throw new ResourceNotFoundException("Product not found: " + request.productId());
        WishlistItem item = new WishlistItem();
        item.setWishlist(wishlist);
        item.setProduct(product);
        wishlist.getWishlistItems().add(item);
        wishlist = wishlistRepository.save(wishlist);
        logger.info("Added item to wishlist ID: {}", wishlistId);
        return mapToResponse(wishlist);
    }

    @Transactional
    public WishlistResponse removeWishlistItem(String wishlistId, String itemId) {
        Wishlist wishlist = wishlistRepository.findByIdNative(wishlistId);
        if (wishlist == null)
            throw new ResourceNotFoundException("Wishlist not found: " + wishlistId);
        wishlist.getWishlistItems().removeIf(item -> item.getId().equals(itemId));
        wishlist = wishlistRepository.save(wishlist);
        logger.info("Removed item {} from wishlist ID: {}", itemId, wishlistId);
        return mapToResponse(wishlist);
    }

    private WishlistResponse mapToResponse(Wishlist wishlist) {
        List<WishlistItemResponse> items = wishlist.getWishlistItems().stream()
                .map(item -> new WishlistItemResponse(
                        item.getId(),
                        item.getWishlist().getId(),
                        item.getProduct().getId()))
                .toList();
        return new WishlistResponse(
                wishlist.getId(),
                wishlist.getUser().getId(),
                wishlist.getCreatedAt(),
                items);
    }

    public WishlistResponse updateWishlist(String id, @Valid WishlistUpdateRequest request) {
        return null;
    }

    public void deleteWishlist(String id) {

    }

    public WishlistResponse getWishlistById(String id) {
        return null;
    }
}