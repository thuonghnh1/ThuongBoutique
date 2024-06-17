package edu.poly.shop.model;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
	private long productId;
	private String name;
	private int quantity;
	private double unitPrice;
	private String image;
		
	private String description;
	private double discount;
	private Date entereDate;
	private short status;
	private long categoryId;
	
	private Boolean isEdit = false;
}
