package edu.poly.shop.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
	@NotEmpty
	private int categoryId;
	@NotEmpty
	@Min(value= 5)
	private String name;
}
