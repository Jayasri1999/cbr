package com.example.cbr.repository;

import org.springframework.stereotype.Repository;

import com.example.cbr.entity.CategoryRouting;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface CategoryRoutingRepository extends JpaRepository<CategoryRouting, String>{
	@Query("SELECT p FROM CategoryRouting p WHERE p.category_name = :category_name AND p.subcategory_name = :subcategory_name")
    Optional<CategoryRouting> findByCatSubCatCntry(@Param("category_name") String category_name, @Param("subcategory_name") String subcategory_name);
}
