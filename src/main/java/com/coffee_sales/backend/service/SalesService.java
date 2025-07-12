package com.coffee_sales.backend.service;
import com.coffee_sales.backend.entity.Coffee;
import com.coffee_sales.backend.entity.Sales;
import com.coffee_sales.backend.entity.AppUser;
import com.coffee_sales.backend.exception.CoffeeServiceException;
import com.coffee_sales.backend.exception.SalesServiceException;
import com.coffee_sales.backend.repository.AppUserRepo;
import com.coffee_sales.backend.repository.CoffeeRepo;
import com.coffee_sales.backend.repository.SalesRepo;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class SalesService {
    @Autowired
    private SalesRepo salesRepo;
    @Autowired
    private CoffeeRepo coffeeRepo;
    @Autowired
    private AppUserRepo appUserRepo;
    @Autowired
    private UserService userService;

    public List<Sales> getAllSalesRecord(){
        try{
            return salesRepo.findAll();
        }catch(DataAccessException e){
            throw new SalesServiceException("Failed to fetch sales record.");
        }
    }

    public Sales getSalesRecordById(Integer id){
        if(id < 0){
            throw new IllegalArgumentException("Id must be positive");
        }
        try{
            return salesRepo.findById(id)
                    .orElseThrow(()-> new SalesServiceException("Sales with ID "+ id + "not found."));
        }catch(DataAccessException e){
            throw new SalesServiceException("Failed to fetch sales record by id.");
        }
    }

    public Integer findTotalSalesCount(){
        try{
            Integer count = salesRepo.countSales();
            return count != null ? count : 0;
        }catch(DataAccessException e){
            throw new SalesServiceException("Failed to fetch total sales count.");
        }
    }

    public Integer findTotalSalesCountBySalesId(Integer id){
        try{
            Integer count = salesRepo.countSalesById(id);
            return count != null ? count : 0;
        }catch(DataAccessException e){
            throw new SalesServiceException("Failed to fetch total sales count by id.");
        }
    }

    public BigDecimal findTotalSalesAmount(){
        try{
            BigDecimal sum = salesRepo.sumSales();
            return sum != null ? sum : BigDecimal.ZERO;
        }catch(DataAccessException e){
            throw new SalesServiceException("Failed to fetch total sales amount.");
        }
    }

    public BigDecimal findTotalSalesAmountById(Integer id){
        try{
            BigDecimal sum = salesRepo.sumSalesById(id);
            return sum != null ? sum : BigDecimal.ZERO;
        }catch(DataAccessException e){
            throw new SalesServiceException("Failed to fetch total sales amount by id.");
        }
    }

    public Sales createSalesEntity(@NotNull Coffee coffee, @NotNull AppUser user) {
        try {
            Sales sale = new Sales();
            Coffee existCoffee = userService.getCoffeeById(coffee.getId());
            sale.setCoffee(existCoffee);
            //TODO: add user new user services for looking up user entity from db
            sale.setAppUser(user);
            return addSales(sale);
        }catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Create Sales Entity:"+e.getMessage());
        }catch(CoffeeServiceException e){
            throw new SalesServiceException("Create Sales Entity: Failed to find coffee.");
        }//TODO: add catch() to catch exception from user related service

    }
    public Sales addSales(@NotNull Sales sales){
        if(sales.getCoffee() == null){
            throw new IllegalArgumentException("Coffee cannot be null.");
        }
        if(sales.getAppUser() == null){
            throw new IllegalArgumentException("User cannot be null.");
        }

        try{
           return salesRepo.save(sales);
        }catch(DataAccessException e){
            throw new SalesServiceException("Failed to insert new sales into the Sales table.");
        }
    }
    //TODO: find most sold coffee
    //TODO: find least sold coffee
    //TODO: find user with most purchase
}
