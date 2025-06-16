package com.github.maximslepukhin.intershop.service;

import com.github.maximslepukhin.intershop.dto.ItemWithCount;
import com.github.maximslepukhin.intershop.model.Item;
import com.github.maximslepukhin.intershop.enums.SortType;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ItemService {
    Page<Item> findItems(int pageSize, int pageNumber, SortType sortType, String searchTitle, String searchDescription);

    Item getItemById(Long id);

    List<Item> getItems(Map<Long, Integer> cart);

    List<ItemWithCount> getItemWithCount(Map<Long, Integer> cart);

    List<ItemWithCount> toItemsWithCount(Page<Item> items, Map<Long, Integer> cart);

    ItemWithCount[][] splitToRows(List<ItemWithCount> itemsWithCount, int ELEMENTS_IN_ROWS);
}