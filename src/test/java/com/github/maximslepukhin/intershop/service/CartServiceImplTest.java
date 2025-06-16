package com.github.maximslepukhin.intershop.service;

import com.github.maximslepukhin.intershop.enums.ActionType;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class CartServiceImplTest {

    private CartServiceImpl cartService;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        cartService = new CartServiceImpl();
        session = mock(HttpSession.class);
    }

    @Test
    void getOrCreateCart_shouldCreateNewCartIfAbsent() {
        when(session.getAttribute("cart")).thenReturn(null);

        Map<Long, Integer> cart = cartService.getOrCreateCart(session);

        assertNotNull(cart);
        assertTrue(cart.isEmpty());
    }

    @Test
    void getOrCreateCart_shouldReturnExistingCart() {
        Map<Long, Integer> existingCart = new HashMap<>();
        existingCart.put(1L, 2);
        when(session.getAttribute("cart")).thenReturn(existingCart);

        Map<Long, Integer> cart = cartService.getOrCreateCart(session);

        assertEquals(existingCart, cart);
    }

    @Test
    void changeCart_shouldAddItemWhenPlusAction() {
        Map<Long, Integer> cart = new HashMap<>();
        Long itemId = 1L;

        Map<Long, Integer> result = cartService.changeCart(cart, ActionType.plus, itemId);

        assertEquals(1, result.get(itemId));
    }

    @Test
    void changeCart_shouldIncrementItemCountWhenPlusAction() {
        Map<Long, Integer> cart = new HashMap<>();
        Long itemId = 1L;
        cart.put(itemId, 2);

        Map<Long, Integer> result = cartService.changeCart(cart, ActionType.plus, itemId);

        assertEquals(3, result.get(itemId));
    }

    @Test
    void changeCart_shouldDecrementItemCountWhenMinusAction() {
        Map<Long, Integer> cart = new HashMap<>();
        Long itemId = 1L;
        cart.put(itemId, 3);

        Map<Long, Integer> result = cartService.changeCart(cart, ActionType.minus, itemId);

        assertEquals(2, result.get(itemId));
    }

    @Test
    void changeCart_shouldRemoveItemWhenMinusActionResultsInZero() {
        Map<Long, Integer> cart = new HashMap<>();
        Long itemId = 1L;
        cart.put(itemId, 1);

        Map<Long, Integer> result = cartService.changeCart(cart, ActionType.minus, itemId);

        assertFalse(result.containsKey(itemId));
    }

    @Test
    void changeCart_shouldRemoveItemWhenDeleteAction() {
        Map<Long, Integer> cart = new HashMap<>();
        Long itemId = 1L;
        cart.put(itemId, 5);

        Map<Long, Integer> result = cartService.changeCart(cart, ActionType.delete, itemId);

        assertFalse(result.containsKey(itemId));
    }

    @Test
    void changeCart_shouldHandleNullCart() {
        Long itemId = 2L;
        Map<Long, Integer> result = cartService.changeCart(null, ActionType.plus, itemId);

        assertNotNull(result);
        assertEquals(1, result.get(itemId));
    }
}