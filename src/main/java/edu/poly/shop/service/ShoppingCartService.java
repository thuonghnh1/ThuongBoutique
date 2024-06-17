package edu.poly.shop.service;

import java.util.Map;

import edu.poly.shop.domain.CartItem;

public interface ShoppingCartService {

	void clear();

	void removeItem(Long productId);

	Map<Long, CartItem> getItems();

	void addItem(CartItem item);

	int getQuantityInCart(Long productId);

}
