package com.mgsystems.mgcatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mgsystems.mgcatalog.dto.CategoryDTO;
import com.mgsystems.mgcatalog.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	public List<CategoryDTO> findAll() {
		
		return repository.findAll().stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	}

}
