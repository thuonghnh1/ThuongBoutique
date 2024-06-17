package edu.poly.shop.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import edu.poly.shop.domain.Product;

@Service
public class CartService {

    private final Map<Long, Product> cart = new HashMap();

    public void addProductToCart(Product product) {
        cart.put(product.getProductId(), product);
    }

    public Collection<Product> getCartItems() {
        return cart.values();
    }

    public void removeProductFromCart(Long productId) {
        cart.remove(productId);
    }
}
