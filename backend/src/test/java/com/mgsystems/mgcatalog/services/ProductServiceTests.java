package com.mgsystems.mgcatalog.services;


import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mgsystems.mgcatalog.dto.ProductDTO;
import com.mgsystems.mgcatalog.entities.Category;
import com.mgsystems.mgcatalog.entities.Product;
import com.mgsystems.mgcatalog.repositories.CategoryRepository;
import com.mgsystems.mgcatalog.repositories.ProductRepository;
import com.mgsystems.mgcatalog.services.exceptions.DatabaseException;
import com.mgsystems.mgcatalog.services.exceptions.ResourceNotFoundException;
import com.mgsystems.mgcatalog.tests.Factory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private Product product;
	private PageImpl<Product> page;
	private ProductDTO dto;
	private Category category;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		product = Factory.createProduct();
		dto = Factory.createProductDTO();
		page= new PageImpl<>(List.of(product));
		category = Factory.createCategory();
		
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		
		
		Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);
		
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
		
		Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
		
		
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		
		ProductDTO result = service.update(existingId, dto);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository).save(product);
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			
			service.update(nonExistingId, dto);
			
		});
		
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		
		ProductDTO result = service.findById(existingId);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository).findById(existingId);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			
			service.findById(nonExistingId);
			
		});
		
		Mockito.verify(repository).findById(nonExistingId);
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		
		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAllPaged(pageable);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository).findAll(pageable);
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		
		Assertions.assertThrows(DatabaseException.class, () ->{
			
			service.delete(dependentId);
			
		});
		
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			
			service.delete(nonExistingId);
			
		});
		
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(() -> {
			
			service.delete(existingId);
			
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}

}
