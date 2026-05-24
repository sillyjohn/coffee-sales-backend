package com.coffee_sales.backend.service;

import com.coffee_sales.backend.entity.Categories;
import com.coffee_sales.backend.entity.Coffee;
import com.coffee_sales.backend.exception.CoffeeServiceException;
import com.coffee_sales.backend.repository.CategoryRepo;
import com.coffee_sales.backend.repository.CoffeeRepo;
import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class CoffeeService {
    @Autowired
    private CoffeeRepo coffeeRepo;
    @Autowired
    private CategoryRepo categoryRepo;

    public List<Coffee> getAllCoffee() {
        try {
            return coffeeRepo.findAll();
        } catch (DataAccessException e) {
            throw new CoffeeServiceException("Failed to fetch coffeelist.", HttpStatus.BAD_REQUEST);
        }
    }

    public Coffee getCoffeeById(Integer id) {
        if (id < 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        return coffeeRepo.findById(id)
                .orElseThrow(() -> new CoffeeServiceException("Coffee with ID " + id + " not found"));
    }

    public Coffee createCoffee(String name, BigDecimal price, Categories categories) {
        return new Coffee(null, name, price, categories, categories.getName());
    }

    public Coffee addCoffee(Coffee coffee) {
        try {
            return coffeeRepo.save(coffee);
        } catch (DataAccessException e) {
            throw new CoffeeServiceException("Coffee Add Failed: Failed to add coffee to database by Coffee Object: " + coffee.getName(), HttpStatus.BAD_REQUEST, e);
        }
    }

    public void removeCoffeeByCoffee(Coffee coffee) {
        if (coffee.getId() == null) {
            throw new CoffeeServiceException("Coffee Remove Failed: Id cannot be null.",HttpStatus.BAD_REQUEST);
        }
        try {
            if (!coffeeRepo.existsById(coffee.getId())) {
                throw new CoffeeServiceException("Coffee Remove: Coffee Id not found.",HttpStatus.NOT_FOUND);
            }
            coffeeRepo.deleteById(coffee.getId());
        } catch (DataAccessException e) {
            throw new CoffeeServiceException("Coffee Remove Failed: Failed to remove a coffee by Coffee Object: " + coffee.getName(), HttpStatus.BAD_REQUEST);
        }
    }

    public void removeCoffeeById(Integer id) {
        try {
            if (!coffeeRepo.existsById(id)) {
                throw new CoffeeServiceException("Coffee with ID " + id + " not found");
            }
            coffeeRepo.deleteById(id);
        } catch (DataAccessException e) {
            throw new CoffeeServiceException("Coffee Remove Failed: Failed to remove a coffee by Coffee Id: " + id, HttpStatus.BAD_REQUEST);
        }
    }

    public Categories getCategory(String categoryName) {
        if (categoryRepo.existsByName(categoryName)) {
            return categoryRepo.findByName(categoryName);
        } else {
            // Create new Category
            Categories newCat = new Categories(null, categoryName);
            categoryRepo.save(newCat);
            return categoryRepo.findByName(categoryName);
        }
    }

    public Categories addCategory(String categoryName) {
        if (categoryRepo.existsByName(categoryName)) {
            throw new CoffeeServiceException("Category Add Failed: Category already exist.",HttpStatus.BAD_REQUEST);
        }
        // Create new Category
        Categories newCat = new Categories(null, categoryName);
        categoryRepo.save(newCat);
        return newCat;
    }

    public void removeCategory(Integer id) {
        if (categoryRepo.existsById(id)) {
            categoryRepo.deleteById(id);
        } else {
            throw new CoffeeServiceException("Category Remove Failed: Failed to remove Category entry.");
        }
    }

    public void modifyCategory(Integer id, String name) {
        if (categoryRepo.existsById(id)) {
            categoryRepo.updateById(id, name);
        } else {
            throw new CoffeeServiceException("Category Modify Failed: Failed to modify Category entry.");
        }
    }
}
