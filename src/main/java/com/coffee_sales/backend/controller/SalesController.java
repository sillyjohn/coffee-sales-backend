package com.coffee_sales.backend.controller;

import com.coffee_sales.backend.dto.SalesRequest;
import com.coffee_sales.backend.entity.Sales;
import com.coffee_sales.backend.exception.SalesServiceException;
import com.coffee_sales.backend.service.SalesService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "http://localhost:3000")
public class SalesController {
    @Autowired
    private SalesService salesService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllSalesRecord(){
        try{
            List<Sales> salesList = salesService.getAllSalesRecord();
            return ResponseEntity.ok(salesList);
        }catch(SalesServiceException e){
            return ResponseEntity.status(500).body(Map.of("error",e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getSalesRecordById(@PathVariable @Positive Integer id){
        try{
            Sales sales = salesService.getSalesRecordById(id);
            return ResponseEntity.ok(sales);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }catch(SalesServiceException e){
            return ResponseEntity.status(404).body(Map.of("error",e.getMessage()));
        }
    }

    @GetMapping("/test/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getSalesRecordTest(@PathVariable @Positive Integer id){
        try{
            Sales sales = salesService.getSalesRecordById(id);
            return ResponseEntity.ok(sales);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }catch(SalesServiceException e){
            return ResponseEntity.status(404).body(Map.of("error",e.getMessage()));
        }
    }

    @GetMapping("/salescount")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getSalesCount(){
        try{
            Integer count = salesService.findTotalSalesCount();
            return ResponseEntity.ok(count);
        }catch(SalesServiceException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }

    @GetMapping("/salescount/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getSalesCountById(@PathVariable @Positive Integer id){
        try{
            Integer count = salesService.findTotalSalesCountBySalesId(id);
            return ResponseEntity.ok(count);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        } catch(SalesServiceException e){
            return ResponseEntity.status(404).body(Map.of("error",e.getMessage()));
        }
    }

    @Transactional
    @PostMapping("/placeorder")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> placeOrder(@Valid @RequestBody SalesRequest salesRequest){
        try{
            if(!salesRequest.isAppUser() && !salesRequest.hasWalkInCustomerInfo()){
                return ResponseEntity.badRequest().body(Map.of("error","Failed to place order: Userinfo not completed."));
            }
            Sales order = salesService.placeOrder(salesRequest);
            return ResponseEntity.ok().body(Map.of("message","Order placed: order id:" + order.getId()));
        }catch(SalesServiceException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }

    @Transactional
    @PostMapping("/createnewsales")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> createNewSales(@Valid @RequestBody SalesRequest salesRequest){
        try{
            Sales createdSale = salesService.createSalesEntity(salesRequest);
            return ResponseEntity.status(201).body("Created new sales "+createdSale.getId());
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }catch(SalesServiceException e){
            return ResponseEntity.status(404).body(Map.of("error",e.getMessage()));
        }
    }

    @Transactional
    @PostMapping("/addsales")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> addSale(@Valid @RequestBody Sales sales){
        try{
            Sales addedSales = salesService.addSales(sales);
            return ResponseEntity.status(201).body(Map.of("message","Sales added into the Sales table."));
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }catch(SalesServiceException e){
            return ResponseEntity.status(404).body(Map.of("error",e.getMessage()));
        }
    }

    @Transactional
    @DeleteMapping("/removesales")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeSales(@PathVariable @Positive Integer id){
        try{
            salesService.removeSalesById(id);
            return ResponseEntity.ok(Map.of("message","sales "+id+" removed successfully."));
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }catch(SalesServiceException e){
            return ResponseEntity.status(404).body(Map.of("error",e.getMessage()));
        }
    }

    @Transactional
    @GetMapping("/mostsold")
    public ResponseEntity<?> mostSoldCoffee(){
        try{
            return ResponseEntity.ok(salesService.findMostSoldCoffee());
        }catch(SalesServiceException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }
    //TODO: findleastsold


}
