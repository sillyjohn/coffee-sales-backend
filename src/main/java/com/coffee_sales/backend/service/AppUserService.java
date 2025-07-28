package com.coffee_sales.backend.service;

import com.coffee_sales.backend.entity.AppUser;
import com.coffee_sales.backend.entity.Sales;
import com.coffee_sales.backend.exception.AppUserServiceException;
import com.coffee_sales.backend.repository.AppUserRepo;
import com.coffee_sales.backend.repository.SalesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AppUserService {
    @Autowired
    private AppUserRepo appUserRepo;
    @Autowired
    private SalesRepo salesRepo;

    public List<AppUser> getUserList(){
        try {
            return appUserRepo.findAll();
        }catch(DataAccessException e){
            throw new AppUserServiceException("Failed to fetch all user in the database.");
        }
    }

    public List<Integer> getUserIdList(){
        try{
            return appUserRepo.findAllUserId();
        }catch(DataAccessException e){
            throw new AppUserServiceException("Failed to fetch all users id on the database");
        }
    }

    public AppUser findUserById(Integer id){
        if(id<0){
            throw new IllegalArgumentException("Id must be positive.");
        }
        try{
          return appUserRepo.findById(id)
                  .orElseThrow(()-> new AppUserServiceException("User with id:" + id + " not exist."));
        }catch(DataAccessException e){
            throw new AppUserServiceException("Failed to fetch user by id.");
        }
    }

    public Integer countUser(){
        try{
            return appUserRepo.countUser();
        }catch(DataAccessException e){
            throw new AppUserServiceException("Failed to fetch user count.");
        }
    }

    public List<Sales> getSalesByUserId(Integer id){
        try{
            return salesRepo.findByAppUser_Id(id);
        }catch(DataAccessException e){
            throw new AppUserServiceException("Failed to fetch the sales list for User: "+id+".");
        }
    }
}
