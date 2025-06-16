package com.github.maximslepukhin.intershop.service;

import com.github.maximslepukhin.intershop.enums.ActionType;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Override
    public Map<Long, Integer> getOrCreateCart(HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @Override
    public Map<Long, Integer> changeCart(Map<Long, Integer> cart, ActionType action, Long id) {
        if (cart == null) {
            cart = new HashMap<>();
        }
        switch (action) {
            case plus:
                cart.put(id, cart.getOrDefault(id, 0) + 1);
                break;
            case minus:
                if (cart.containsKey(id)) {
                    int count = cart.get(id);
                    if (count > 1) {
                        cart.put(id, count - 1);
                    } else {
                        cart.remove(id);
                    }
                }
                break;
            case delete:
                cart.remove(id);
                break;
            default:
                break;
        }
        return cart;
    }
}
