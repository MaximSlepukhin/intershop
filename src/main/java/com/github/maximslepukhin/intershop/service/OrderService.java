package com.github.maximslepukhin.intershop.service;

import com.github.maximslepukhin.intershop.model.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    List<Order> findAll();

    Order getOrderById(Long id);

    Long createOrderFromCart(Map<Long, Integer> cart);
}
