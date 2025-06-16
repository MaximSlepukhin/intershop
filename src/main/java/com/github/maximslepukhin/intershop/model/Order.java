package com.github.maximslepukhin.intershop.model;

import com.github.maximslepukhin.intershop.dto.ItemWithCount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_sum")
    private double totalSum;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public Long id() {
        return getId();
    }

    public double totalSum() {
        return getTotalSum();
    }

    @Transient
    public List<ItemWithCount> items() {
        return orderItems.stream()
                .map(oi -> new ItemWithCount(oi.getItem(), oi.getCount()))
                .collect(Collectors.toList());
    }
}