package com.mgsystems.mgcatalog.tests;

import java.time.Instant;

import com.mgsystems.mgcatalog.dto.CategoryDTO;
import com.mgsystems.mgcatalog.dto.ProductDTO;
import com.mgsystems.mgcatalog.entities.Category;
import com.mgsystems.mgcatalog.entities.Product;

public class Factory {
	
	public static Product createProduct() {
		
		Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2023-12-29T07:15:00Z"));
		product.getCategories().add(createCategory());
		
		return product;
	}
	
	public static ProductDTO createProductDTO() {
		
		Product product = createProduct();
		
		return new ProductDTO(product, product.getCategories());
	}
	
	public static Category createCategory() {
		
		Category category= new Category(2L, "Electronics");
		
		return category;
	}
	
	public static CategoryDTO createCategoryDTO() {
		
		CategoryDTO categoryDTO = new CategoryDTO(createCategory());
		
		return categoryDTO;
	}

}
