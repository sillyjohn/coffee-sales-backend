package com.coffee_sales.backend.service;
import com.coffee_sales.backend.entity.Coffee;
import com.coffee_sales.backend.exception.CoffeeServiceException;
import com.coffee_sales.backend.repository.CoffeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private CoffeeRepo coffeeRepo;

    public List<Coffee> getAllCoffee(){
        try{
            return coffeeRepo.findAll();
        }catch(DataAccessException e){
            throw new CoffeeServiceException("Failed to fetch coffeelist.");
        }
    }

    public Coffee getCoffeeById(Integer id){
        if(id < 0){
            throw new IllegalArgumentException("Id must be positive");
        }
        try{
            return coffeeRepo.findById(id)
                             .orElseThrow(() -> new CoffeeServiceException("Coffee with ID " + id + " not found"));
        }catch(DataAccessException e){
            throw new CoffeeServiceException("Failed to fetch coffee "+id+" by id.", e );
        }
    }

    public Coffee addCoffee(Coffee coffee){
        if(coffee.getPrice() == null){
            throw new IllegalArgumentException("Price cannot be null.");
        }
        if(coffee.getName() == null){
            throw new IllegalArgumentException("Name cannot be null.");
        }
        if(coffee.getPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Price must be positive.");
        }
        try{
            return coffeeRepo.save(coffee);
        }catch(DataAccessException e) {
            throw new CoffeeServiceException("Failed to add coffee to database by Coffee Object: "+coffee.getName(),e);
        }
    }

    public void removeCoffeeByCoffee(Coffee coffee){
        if(coffee.getId() == null){
            throw new IllegalArgumentException("Id cannot be null.");
        }
        try{
            if(!coffeeRepo.existsById(coffee.getId())){
                throw new CoffeeServiceException("Coffee not found.");
            }
            coffeeRepo.deleteById(coffee.getId());
        }catch(DataAccessException e){
            throw new CoffeeServiceException("Failed to remove a coffee by Coffee Object: "+ coffee.getName(),e);
        }
    }

    public void removeCoffeeById(Integer id){
        if(id < 0){
            throw new IllegalArgumentException("Id must be positive");
        }
        try{
            if (!coffeeRepo.existsById(id)) {
                throw new CoffeeServiceException("Coffee with ID " + id + " not found");
            }
            coffeeRepo.deleteById(id);
        }catch(DataAccessException e){
            throw new CoffeeServiceException("Failed to remove a coffee by Coffee Id: "+id, e);
        }
    }


}
