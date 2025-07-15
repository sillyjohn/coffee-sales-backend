package com.coffee_sales.backend.controller;

import com.coffee_sales.backend.entity.AppUser;
import com.coffee_sales.backend.entity.Coffee;
import com.coffee_sales.backend.entity.Sales;
import com.coffee_sales.backend.exception.SalesServiceException;
import com.coffee_sales.backend.service.SalesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getAllSalesRecord(){
        try{
            List<Sales> salesList = salesService.getAllSalesRecord();
            return ResponseEntity.ok(salesList);
        }catch(SalesServiceException e){
            return ResponseEntity.status(500).body(Map.of("error",e.getMessage()));
        }
    }

    @GetMapping("/{id}")
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

    @GetMapping("/salescount")
    public ResponseEntity<?> getSalesCount(){
        try{
            Integer count = salesService.findTotalSalesCount();
            return ResponseEntity.ok(count);
        }catch(SalesServiceException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }

    @GetMapping("/salescount/{id}")
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

    @PostMapping("/createnewsales")
    public ResponseEntity<?> createNewSales(@Valid @RequestBody Coffee coffee, @Valid @RequestBody AppUser appUser){
        try{
            Sales createdSale = salesService.createSalesEntity(coffee,appUser);
            return ResponseEntity.status(201).body("Created new sales "+createdSale.getId());
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }catch(SalesServiceException e){
            return ResponseEntity.status(404).body(Map.of("error",e.getMessage()));
        }
    }

    @PostMapping("/addsales")
    public ResponseEntity<?> addSale(@Valid @RequestBody Sales sales){
        try{
            Sales addedSales = salesService.addSales(sales);
            return ResponseEntity.ok().body(Map.of("message","Sales added into the Sales table."));
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }catch(SalesServiceException e){
            return ResponseEntity.status(404).body(Map.of("error",e.getMessage()));
        }
    }
    //TODO: removesalse
    //TODO: findmostsold
    //TODO: findleastsold


}
