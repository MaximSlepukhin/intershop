package com.github.maximslepukhin.intershop.controller;

import com.github.maximslepukhin.intershop.ActionType;
import com.github.maximslepukhin.intershop.SortType;
import com.github.maximslepukhin.intershop.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class CartController {
    private final ItemServiceImpl itemService;
    private final OrderServiceImpl orderService;

    public CartController(ItemServiceImpl itemService, OrderServiceImpl orderService) {
        this.itemService = itemService;
        this.orderService = orderService;
    }

    //а) редирект на главную
    @GetMapping("/")
    public String redirectToMain() {
        return "redirect:/main/items";
    }

    //б) список всех товаров плиткой на главной странице
    @GetMapping("/main/items")
    public String showItems(@RequestParam(defaultValue = "") String search,
                            @RequestParam(defaultValue = "NO") SortType sort,
                            @RequestParam(defaultValue = "10") int pageSize,
                            @RequestParam(defaultValue = "1") int pageNumber,
                            Model model) {
        return "main";
    }

    //в) изменить количество товара в корзине
    @PostMapping("/main/items/{id}")
    public String updateItemFromMain(@PathVariable Long id,
                                     @RequestParam ActionType action,
                                     RedirectAttributes redirectAttributes) {
        return "redirect:/main/items";
    }

    //г) список товаров в корзине
    @GetMapping("/cart/items")
    public String showCart(Model model) {
        return "cart";
    }

    //д) изменить количество товара в корзине
    @PostMapping("/cart/items/{id}")
    public String updateItemFromCart(@PathVariable Long id,
                                     @RequestParam ActionType action) {
        return "redirect:/cart/items";
    }

    //е) карточка товара
    @GetMapping("items/{id}")
    public String showItem(@PathVariable Long id, Model model) {
        return "item";
    }

    //ж) изменить количество товара в корзине
    @PostMapping("/items/{id}")
    public String updateItemFromItemPage(@PathVariable Long id,
                                         @RequestParam String action) {
        return "redirect:/items" + id;
    }

    //з) купить товары в корзине (выполняет покупку товаров в корзине и оцищает ее)
    @PostMapping("/buy")
    public String buyItem() {
        long orderId = 1L;
        return "redirect:/orders/" + orderId + "?newOrder=true";
    }

    //и) список заказов
    @GetMapping("/orders")
    public String showOrders(Model model) {
        return "orders";
    }

    //к) карточка заказа
    @GetMapping("/orders/{id}")
    public String showOrder(@PathVariable Long id,
                            @RequestParam(defaultValue = "false") boolean newOrder,
                            Model model) {
        return "order";
    }
}
