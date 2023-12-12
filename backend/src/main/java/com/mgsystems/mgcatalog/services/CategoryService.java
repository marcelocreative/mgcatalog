package com.mgsystems.mgcatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgsystems.mgcatalog.dto.CategoryDTO;
import com.mgsystems.mgcatalog.entities.Category;
import com.mgsystems.mgcatalog.exceptions.ResourceNotFoundException;
import com.mgsystems.mgcatalog.repositories.CategoryRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		
		return repository.findAll().stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow( ()-> new ResourceNotFoundException("Entity not found"));
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		
		Category entity= new Category();
		entity.setName(dto.getName());
		
		entity = repository.save(entity);
		
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		
		try {
			Category entity = repository.getReferenceById(id);
			entity.setName(dto.getName());
			entity = repository.save(entity);
			
			return new CategoryDTO(entity);
		}
		catch (EntityNotFoundException e) {
			
			throw new ResourceNotFoundException("Id not found "+id);
			
		}
	}

}
