package com.coffee_sales.backend.repository;
import com.coffee_sales.backend.entity.Categories;
import com.coffee_sales.backend.entity.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoffeeRepo extends JpaRepository<Coffee,Integer> {
}
