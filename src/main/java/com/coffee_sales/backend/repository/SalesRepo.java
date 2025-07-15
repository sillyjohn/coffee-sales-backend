package com.coffee_sales.backend.repository;
import com.coffee_sales.backend.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;

@Repository
public interface SalesRepo extends JpaRepository<Sales,Integer> {
    @Query("SELECT SUM(s.coffee.price) FROM Sales s")
    BigDecimal sumSales();

    @Query("SELECT SUM(s.coffee.price) FROM Sales s WHERE s.id = :id")
    BigDecimal sumSalesById(@Param("id") Integer id);

    @Query("SELECT COUNT(s.id) FROM Sales s")
    Integer countSales();

    @Query("SELECT COUNT(s.id) FROM Sales s WHERE s.id = :id")
    Integer countSalesById(@Param("id") Integer id);

    @Query(value = "SELECT coffee_id FROM sales GROUP BY coffee_id ORDER BY COUNT(*) DESC LIMIT 1", nativeQuery = true)
    Integer countMostSoldCoffeeId();

    @Query(value = "SELECT coffee_id FROM sales GROUP BY coffee_id ORDER BY COUNT(*) ASC LIMIT 1", nativeQuery = true)
    Integer countLeastSoldCoffeeId();

    //TODO: user with most purchase custom query
    @Query(value = "SELECT user_id FROM sales GROUP BY user_id ORDER BY COUNT(*) DESC LIMIT 1", nativeQuery = true)
    Integer countMostFrequentUser();
}
