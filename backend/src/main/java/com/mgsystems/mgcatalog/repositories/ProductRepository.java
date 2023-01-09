package com.mgsystems.mgcatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mgsystems.mgcatalog.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
