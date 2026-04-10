package com.coffee_sales.backend.repository;

import com.coffee_sales.backend.entity.Order;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order,Integer> {
    Optional<Order> findByappUserID(Integer appUserID);

}
