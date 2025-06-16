package com.github.maximslepukhin.intershop.service;

import com.github.maximslepukhin.intershop.dto.ItemWithCount;
import com.github.maximslepukhin.intershop.enums.SortType;
import com.github.maximslepukhin.intershop.model.Item;
import com.github.maximslepukhin.intershop.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ItemServiceImplTest {
    private ItemRepository itemRepository;
    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        itemRepository = mock(ItemRepository.class);
        itemService = new ItemServiceImpl(itemRepository);
    }

    @Test
    void findItems_shouldReturnSortedPage() {
        List<Item> items = List.of(new Item(1L, "Ноутбук Lenovo", "15.6\", 16 ГБ RAM, SSD 512 ГБ",
                80000, "images/lenovo.jpeg"));
        Page<Item> page = new PageImpl<>(items);
        when(itemRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                anyString(), anyString(), any(Pageable.class))
        ).thenReturn(page);

        Page<Item> result = itemService.findItems(10, 1, SortType.ALPHA, "item", "item");

        assertEquals(1, result.getContent().size());
        verify(itemRepository).findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(eq("item"), eq("item"), any());
    }

    @Test
    void getItemById_shouldReturnItemIfExists() {
        Item item = new Item(1L, "Ноутбук Lenovo", "15.6\", 16 ГБ RAM, SSD 512 ГБ",
                80000, "images/lenovo.jpeg");
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Item result = itemService.getItemById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getItemById_shouldReturnNullIfNotFound() {
        when(itemRepository.findById(2L)).thenReturn(Optional.empty());

        Item result = itemService.getItemById(2L);

        assertNull(result);
    }

    @Test
    void getItems_shouldReturnItemsFromCart() {

        Map<Long, Integer> cart = Map.of(1L, 2, 2L, 1);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(new Item(1L, "Ноутбук Lenovo", "15.6\", 16 ГБ RAM, SSD 512 ГБ",
                80000, "images/lenovo.jpeg")));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(new Item(2L, "Смартфон Samsung", "6.5\" AMOLED, 128 ГБ памяти",
                90000, "images/samsung.jpeg")));

        List<Item> result = itemService.getItems(cart);

        assertEquals(2, result.size());
    }

    @Test
    void getItemWithCount_shouldReturnCorrectCounts() {
        Map<Long, Integer> cart = Map.of(1L, 3);
        Item item = new Item(1L, "Ноутбук Lenovo", "15.6\", 16 ГБ RAM, SSD 512 ГБ",
                80000, "images/lenovo.jpeg");
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        List<ItemWithCount> result = itemService.getItemWithCount(cart);

        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getCount());
        assertEquals(item, result.getFirst());
    }

    @Test
    void toItemsWithCount_shouldMapItemsToItemWithCount() {
        Map<Long, Integer> cart = Map.of(1L, 2);
        Item item = new Item(1L, "Ноутбук Lenovo", "15.6\", 16 ГБ RAM, SSD 512 ГБ",
                80000, "images/lenovo.jpeg");
        Page<Item> page = new PageImpl<>(List.of(item));

        List<ItemWithCount> result = itemService.toItemsWithCount(page, cart);

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getCount());
    }

    @Test
    void splitToRows_shouldSplitItemsCorrectly() {
        List<ItemWithCount> items = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Item item = new Item(1L, "Ноутбук Lenovo", "15.6\", 16 ГБ RAM, SSD 512 ГБ",
                    80000, "images/lenovo.jpeg");
            items.add(new ItemWithCount(item, i + 1));
        }

        ItemWithCount[][] result = itemService.splitToRows(items, 3);

        assertEquals(3, result.length);
        assertEquals(3, result[0].length);
        assertEquals(3, result[1].length);
        assertEquals(1, result[2].length);
    }
}