package edu.poly.shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
	private long productId;
	private String name;
	private int quantity;
	private double unitPrice;
	private double totalPrice;
}
