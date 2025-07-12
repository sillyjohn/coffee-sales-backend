package com.coffee_sales.backend.entity;

import jakarta.persistence.*;
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
    @JoinColumn(name = "user_id", nullable = false)
    AppUser appUser;
    @Column(name = "sales_date", nullable = false)
    private LocalDateTime salesDate;

    @PrePersist
    protected void onCreate(){
        this.salesDate = LocalDateTime.now();
    }
}
// sale, coffe, usr, date
// 1, 3, john, 9/7/2025 10:00am