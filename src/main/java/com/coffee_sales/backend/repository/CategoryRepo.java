package com.coffee_sales.backend.repository;

import com.coffee_sales.backend.entity.Categories;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Categories,Integer> {
    Categories findByName(String name);
    boolean existsByName(String name);
    @Transactional
    @Modifying
    @Query("UPDATE Categories c SET c.name = :name WHERE c.id = :id")
    String updateById(@Param("id") Integer id, @Param("name") String categoryName);
}
