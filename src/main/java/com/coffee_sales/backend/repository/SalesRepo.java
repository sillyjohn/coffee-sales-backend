package com.coffee_sales.backend.repository;
import com.coffee_sales.backend.entity.Coffee;
import com.coffee_sales.backend.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;

@Repository
public interface SalesRepo extends JpaRepository<Sales,Integer> {
    @Query("SELECT SUM(s.price) FROM Sales s")
    BigDecimal sumSales();

    @Query("SELECT SUM(s.price) FROM Sales s WHERE s.id = :id")
    BigDecimal sumSalesById(@Param("id") Integer id);

    @Query("SELECT COUNT(s.id) FROM Sales s")
    Integer countSales();

    @Query("SELECT COUNT(s.id) FROM Sales s WHERE s.id = :id")
    Integer countSalesById(@Param("id") Integer id);

    @Query("SELECT MAX(s.coffee_id) FROM Sales s")
    Coffee countMostSoldCoffee();

    //TODO: least sold coffee custom query
    //TODO: user with most purchase custom query
}
