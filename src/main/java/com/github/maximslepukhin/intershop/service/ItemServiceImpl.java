package com.github.maximslepukhin.intershop.service;

import com.github.maximslepukhin.intershop.dto.ItemWithCount;
import com.github.maximslepukhin.intershop.model.Item;
import com.github.maximslepukhin.intershop.enums.SortType;
import com.github.maximslepukhin.intershop.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Page<Item> findItems(int pageSize, int pageNumber, SortType sortType, String searchTitle, String searchDescription) {
        Sort sort = switch (sortType) {
            case ALPHA -> Sort.by("title").ascending();
            case PRICE -> Sort.by("price").ascending();
            case NO -> Sort.unsorted();
        };
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<Item> page = itemRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchTitle, searchDescription, pageable);
        return page;
    }

    @Override
    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    @Override
    public List<Item> getItems(Map<Long, Integer> cart) {
        List<Item> items = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Long itemId = entry.getKey();
            Item item = itemRepository.findById(itemId).orElse(null);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }

    @Override
    public List<ItemWithCount> getItemWithCount(Map<Long, Integer> cart) {
        List<Item> items = getItems(cart);
        List<ItemWithCount> itemsWithCount = items.stream()
                .map(item -> {
                    int count = cart.getOrDefault(item.getId(), 0);
                    return new ItemWithCount(item, count);
                })
                .toList();
        return itemsWithCount;
    }

    @Override
    public List<ItemWithCount> toItemsWithCount(Page<Item> allItems, Map<Long, Integer> cart) {
        List<ItemWithCount> itemsWithCount = allItems.stream()
                .map(item -> {
                    int count = cart.getOrDefault(item.getId(), 0);
                    return new ItemWithCount(item, count);
                })
                .toList();
        return itemsWithCount;
    }

    @Override
    public ItemWithCount[][] splitToRows(List<ItemWithCount> itemsWithCount, int ELEMENTS_IN_ROWS) {
        int rowsCount = (itemsWithCount.size() + ELEMENTS_IN_ROWS) / ELEMENTS_IN_ROWS;
        ItemWithCount[][] items = new ItemWithCount[rowsCount][0];
        int k = 0;
        for (int i = 0; i < rowsCount; i++) {
            int rowSize = ELEMENTS_IN_ROWS;
            if (i == rowsCount - 1) {
                rowSize = itemsWithCount.size() % ELEMENTS_IN_ROWS;
            }
            ItemWithCount[] row = new ItemWithCount[rowSize];
            for (int j = 0; j < rowSize; j++) {
                row[j] = itemsWithCount.get(k);
                k++;
            }
            items[i] = row;
        }
        return items;
    }
}
