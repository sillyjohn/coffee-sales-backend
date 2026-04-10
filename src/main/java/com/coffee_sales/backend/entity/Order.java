package com.coffee_sales.backend.entity;


import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "customer order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    //jsonb for Postgresql
    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "items json", columnDefinition = "jsonb")
    List<OrderItem> items;

    @Column(name = "Buyer ID")
    Integer appUserID;

    @Enumerated(EnumType.STRING)
    @Column(name = "Order Status")
    private OrderStatus orderStatus;
    @Column(name = "Order time")
    private LocalDateTime orderCreateTime;
}
