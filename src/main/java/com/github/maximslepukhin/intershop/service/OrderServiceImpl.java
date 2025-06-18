package com.github.maximslepukhin.intershop.service;

import com.github.maximslepukhin.intershop.model.Item;
import com.github.maximslepukhin.intershop.model.Order;
import com.github.maximslepukhin.intershop.model.OrderItem;
import com.github.maximslepukhin.intershop.model.OrderItemId;
import com.github.maximslepukhin.intershop.repository.ItemRepository;
import com.github.maximslepukhin.intershop.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @Override
    @Transactional
    public Long createOrderFromCart(Map<Long, Integer> cart) {
        List<Item> items = itemRepository.findAllById(cart.keySet());
        Order order = new Order();
        List<OrderItem> orderItems = new ArrayList<>();
        double totalSum = 0;
        for (Item item : items) {
            int count = cart.get(item.getId());
            if (count <= 0) continue;
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItem(item);
            orderItem.setCount(count);
            orderItem.setId(new OrderItemId(null, item.getId()));
            orderItems.add(orderItem);
            totalSum += item.getPrice() * count;
        }
        order.setTotalSum(totalSum);
        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        for (OrderItem orderItem : savedOrder.getOrderItems()) {
            orderItem.getId().setOrderId(savedOrder.getId());
        }
        return savedOrder.getId();
    }
}

