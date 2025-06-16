package com.github.maximslepukhin.intershop.controller;

import com.github.maximslepukhin.intershop.dto.ItemWithCount;
import com.github.maximslepukhin.intershop.dto.ItemsResult;
import com.github.maximslepukhin.intershop.model.Item;
import com.github.maximslepukhin.intershop.enums.SortType;
import com.github.maximslepukhin.intershop.service.ItemService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private static final String CART_SESSION_KEY = "cart";
    private static final int ELEMENTS_IN_ROWS = 3;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/")
    public String redirectToMain() {
        return "redirect:/main/items";
    }

    @GetMapping("/main/items")
    public String showItems(@RequestParam(defaultValue = "") String search,
                            @RequestParam(defaultValue = "NO") SortType sort,
                            @RequestParam(defaultValue = "10") int pageSize,
                            @RequestParam(defaultValue = "1") int pageNumber,
                            HttpSession session,
                            Model model) {
        Map<Long, Integer> cart = Optional.ofNullable((Map<Long, Integer>) session.getAttribute(CART_SESSION_KEY))
                .orElseGet(HashMap::new);
        Page<Item> allItems = itemService.findItems(pageSize, pageNumber, sort, search, search);
        List<ItemWithCount> itemsWithCount = itemService.toItemsWithCount(allItems, cart);
        ItemWithCount[][] items = itemService.splitToRows(itemsWithCount, ELEMENTS_IN_ROWS);
        ItemsResult itemsResult = new ItemsResult(allItems.hasNext(), allItems.getNumber() + 1, pageSize);
        model.addAttribute("items", items);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort.toString());
        model.addAttribute("paging", itemsResult.getPageInfo());
        return "main";
    }

    @GetMapping("items/{id}")
    public String showItem(@PathVariable Long id,
                           Model model, HttpSession session) {
        Item item = itemService.getItemById(id);
        Map<Long, Integer> cart = Optional.ofNullable((Map<Long, Integer>) session.getAttribute(CART_SESSION_KEY))
                .orElseGet(HashMap::new);
        int countFromCart = cart.getOrDefault(item.getId(), 0);
        ItemWithCount itemWithCount = new ItemWithCount(item, countFromCart);
        model.addAttribute("item", itemWithCount);
        return "item";
    }
}
