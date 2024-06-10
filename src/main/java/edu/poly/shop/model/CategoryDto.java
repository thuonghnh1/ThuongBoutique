package edu.poly.shop.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto implements Serializable {
	private long categoryId;
	@NotEmpty()
	@Length(min = 5)
	private String name;

	private Boolean isEdit = false;
}
