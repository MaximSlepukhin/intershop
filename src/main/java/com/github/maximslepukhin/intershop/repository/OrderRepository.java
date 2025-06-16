package com.github.maximslepukhin.intershop.repository;

import com.github.maximslepukhin.intershop.model.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findAll();
}
