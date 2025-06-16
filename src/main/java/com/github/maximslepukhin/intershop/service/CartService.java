package com.github.maximslepukhin.intershop.service;

import com.github.maximslepukhin.intershop.enums.ActionType;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

public interface CartService {
    Map<Long, Integer> getOrCreateCart(HttpSession session);

    Map<Long, Integer> changeCart(Map<Long, Integer> cart, ActionType action, Long id);
}
