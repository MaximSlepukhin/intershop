package com.github.maximslepukhin.intershop.service;

import com.github.maximslepukhin.intershop.model.Item;
import com.github.maximslepukhin.intershop.model.Order;
import com.github.maximslepukhin.intershop.model.OrderItem;
import com.github.maximslepukhin.intershop.repository.ItemRepository;
import com.github.maximslepukhin.intershop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void createOrderFromCart_shouldSaveOrderWithProperSumAndItems() {
        Map<Long, Integer> cart = Map.of(1L, 2, 2L, 3);

        Item item1 = Item.builder().id(1L).price(100.0).build();
        Item item2 = Item.builder().id(2L).price(200.0).build();
        when(itemRepository.findAllById(cart.keySet()))
                .thenReturn(List.of(item1, item2));

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        Order savedOrder = new Order();
        savedOrder.setId(10L);
        when(orderRepository.save(orderCaptor.capture())).thenReturn(savedOrder);

        Long result = orderService.createOrderFromCart(cart);

        assertEquals(10L, result);

        Order captured = orderCaptor.getValue();
        assertEquals(100.0 * 2 + 200.0 * 3, captured.getTotalSum(), 1e-6);
        assertEquals(2, captured.getOrderItems().size());

        assertTrue(captured.getOrderItems().stream().anyMatch(oi ->
                oi.getItem().equals(item1) && oi.getCount() == 2
        ));
        assertTrue(captured.getOrderItems().stream().anyMatch(oi ->
                oi.getItem().equals(item2) && oi.getCount() == 3
        ));

        verify(itemRepository).findAllById(cart.keySet());
    }
}