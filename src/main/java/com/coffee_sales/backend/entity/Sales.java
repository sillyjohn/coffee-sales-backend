package com.coffee_sales.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "sales")
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "coffee_id", nullable = false)
    private Coffee coffee;
    @ManyToOne
    @Nullable
    @JoinColumn(name = "user_id")
    @JsonBackReference
    AppUser appUser;

    //Fields for walk-in customers
    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_phone")
    private Long customerPhone;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "sales_date", nullable = false)
    private LocalDateTime salesDate;

    @PrePersist
    protected void onCreate(){
        this.salesDate = LocalDateTime.now();
    }
}
// sale, coffe, usr, date
// 1, 3, john, 9/7/2025 10:00am