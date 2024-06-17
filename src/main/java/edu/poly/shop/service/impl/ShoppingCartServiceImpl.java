package edu.poly.shop.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import edu.poly.shop.domain.CartItem;
import edu.poly.shop.service.ShoppingCartService;

@Service
@SessionScope
public class ShoppingCartServiceImpl implements ShoppingCartService {
	private Map<Long, CartItem> cartItems = new HashMap<>();

	@Override
	public void addItem(CartItem item) {
		CartItem existingItem = cartItems.get(item.getProductId());
		if (existingItem != null) {
			existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
			existingItem.setTotalPrice(existingItem.getQuantity() * existingItem.getUnitPrice());
		} else {
			item.setTotalPrice(item.getQuantity() * item.getUnitPrice());
			cartItems.put(item.getProductId(), item);
		}
	}

	@Override
	public Map<Long, CartItem> getItems() {
		return cartItems;
	}

	@Override
	public void removeItem(Long productId) {
		cartItems.remove(productId);
	}

	@Override
	public void clear() {
		cartItems.clear();
	}
	
	@Override
	public int getQuantityInCart(Long productId) {
        int quantity = 0;
        if (cartItems.containsKey(productId)) {
            quantity = cartItems.get(productId).getQuantity();
        }
        return quantity;
    }
}
