package theo.dev.ecommerce.services.cart;

import theo.dev.ecommerce.dto.cart.*;
import theo.dev.ecommerce.exceptions.ResourceNotFoundException;
import theo.dev.ecommerce.models.auth.User;
import theo.dev.ecommerce.models.cart.Cart;
import theo.dev.ecommerce.models.cart.CartItem;
import theo.dev.ecommerce.models.product.Product;
import theo.dev.ecommerce.repositories.cart.CartRepository;
import theo.dev.ecommerce.repositories.product.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public CartResponse createCart(CartCreateRequest request) {
        Cart cart = new Cart();
        cart.setUser(new User());
        cart = cartRepository.save(cart);
        logger.info("Created cart with ID: {}", cart.getId());
        return mapToResponse(cart);
    }

    @Transactional
    public CartResponse addCartItem(String cartId, CartItemCreateRequest request) {
        Cart cart = cartRepository.findByIdNative(cartId);
        if (cart == null)
            throw new ResourceNotFoundException("Cart not found: " + cartId);
        Product product = productRepository.findByIdNative(request.productId());
        if (product == null)
            throw new ResourceNotFoundException("Product not found: " + request.productId());
        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(request.quantity());
        cart.getCartItems().add(item);
        cart = cartRepository.save(cart);
        logger.info("Added item to cart ID: {}", cartId);
        return mapToResponse(cart);
    }

    @Transactional
    public CartResponse removeCartItem(String cartId, String itemId) {
        Cart cart = cartRepository.findByIdNative(cartId);
        if (cart == null)
            throw new ResourceNotFoundException("Cart not found: " + cartId);
        cart.getCartItems().removeIf(item -> item.getId().equals(itemId));
        cart = cartRepository.save(cart);
        logger.info("Removed item {} from cart ID: {}", itemId, cartId);
        return mapToResponse(cart);
    }

    // Get, update, delete
    private CartResponse mapToResponse(Cart cart) {
        List<CartItemResponse> items = cart.getCartItems().stream()
                .map(item -> new CartItemResponse(
                        item.getId(),
                        item.getCart().getId(),
                        item.getProduct().getId(),
                        item.getQuantity()))
                .toList();
        return new CartResponse(
                cart.getId(),
                cart.getUser().getId(),
                cart.getCreatedAt(),
                cart.getUpdatedAt(),
                items);
    }

    public CartResponse updateCart(String id, @Valid CartUpdateRequest request) {
        return null;
    }

    public void deleteCart(String id) {
    }

    public CartResponse getCartById(String id) {
        return null;
    }
}