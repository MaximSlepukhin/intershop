package com.github.maximslepukhin.intershop.controller;

import com.github.maximslepukhin.intershop.dto.ItemWithCount;
import com.github.maximslepukhin.intershop.enums.SortType;
import com.github.maximslepukhin.intershop.model.Item;
import com.github.maximslepukhin.intershop.enums.ActionType;
import com.github.maximslepukhin.intershop.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@Slf4j
public class CartController {
    private final ItemService itemService;
    private final OrderService orderService;
    private final CartService cartService;
    private static final String CART_SESSION_KEY = "cart";

    public CartController(ItemService itemService, OrderService orderService, CartService cartService) {
        this.itemService = itemService;
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @PostMapping("/main/items/{id}")
    public String updateItemFromMain(@PathVariable Long id,
                                     @RequestParam ActionType action,
                                     @RequestParam(defaultValue = "") String search,
                                     @RequestParam(defaultValue = "NO") SortType sort,
                                     @RequestParam(defaultValue = "10") int pageSize,
                                     @RequestParam(defaultValue = "1") int pageNumber,
                                     HttpSession session) {
        updateCart(session, action, id);
        return "redirect:/main/items?search=%s&sort=%s&pageSize=%s&pageNumber=%s"
                .formatted(search, sort, pageSize, pageNumber);
    }

    @PostMapping("/cart/items/{id}")
    public String updateItemFromCart(@PathVariable Long id,
                                     @RequestParam ActionType action,
                                     HttpSession session) {
        updateCart(session, action, id);
        return "redirect:/cart/items";
    }

    @PostMapping("/items/{id}")
    public String updateItemFromItemPage(@PathVariable Long id,
                                         @RequestParam ActionType action,
                                         HttpSession session) {
        updateCart(session, action, id);
        return "redirect:/items/" + id;
    }

    @GetMapping("/cart/items")
    public String showCart(Model model, HttpSession session) {
        Map<Long, Integer> cart = cartService.getOrCreateCart(session);
        List<Item> items = itemService.getItems(cart);
        List<ItemWithCount> itemsWithCount = itemService.getItemWithCount(cart);
        model.addAttribute("total", itemsWithCount.stream()
                .mapToDouble(i -> i.getPrice() * i.getCount())
                .sum()
        );
        model.addAttribute("items", itemsWithCount);
        model.addAttribute("empty", items.isEmpty());
        return "cart";
    }

    @PostMapping("/buy")
    public String buyItem(HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null || cart.isEmpty()) {
            return "redirect:/main/items";
        }
        Long orderId = orderService.createOrderFromCart(cart);
        session.removeAttribute(CART_SESSION_KEY);
        return "redirect:/orders/" + orderId + "?newOrder=true";
    }

    private void updateCart(HttpSession session, ActionType action, Long id) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute(CART_SESSION_KEY);
        cart = cartService.changeCart(cart, action, id);
        session.setAttribute(CART_SESSION_KEY, cart);
    }
}
