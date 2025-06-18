package com.github.maximslepukhin.intershop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class CartControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldUpdateItemFromMain() throws Exception {
        MockHttpSession session = new MockHttpSession();
        int itemId = 1;
        mockMvc.perform(post("/main/items/" + itemId)
                        .param("action", "plus")
                        .param("search", "")
                        .param("sort", "NO")
                        .param("pageSize", "10")
                        .param("pageNumber", "1")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/main/items?search=&sort=NO&pageSize=10&pageNumber=1"));
        Map<Long, Integer> updatedCart = (Map<Long, Integer>) session.getAttribute("cart");
        assertNotNull(updatedCart);
        assertTrue(updatedCart.containsKey(1L));
    }

    @Test
    void updateItemFromMain() throws Exception {
        MockHttpSession session = new MockHttpSession();
        int itemId = 1;
        mockMvc.perform(post("/cart/items/" + itemId)
                        .param("action", "plus")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/items"));
        Map<Long, Integer> updatedCart = (Map<Long, Integer>) session.getAttribute("cart");
        assertNotNull(updatedCart);
        assertTrue(updatedCart.containsKey(1L));
    }

    @Test
    void updateItemFromItemPage() throws Exception {
        MockHttpSession session = new MockHttpSession();
        int itemId = 1;
        mockMvc.perform(post("/items/" + itemId)
                        .param("action", "plus")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items/1"));
        Map<Long, Integer> updatedCart = (Map<Long, Integer>) session.getAttribute("cart");
        assertNotNull(updatedCart);
        assertTrue(updatedCart.containsKey(1L));
    }

    @Test
    void shouldShowCart() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("cart", new HashMap<Long, Integer>());
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        cart.put(1L, 2);
        mockMvc.perform(get("/cart/items")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attribute("empty", false))
                .andExpect(model().attribute("items", hasSize(1)))
                .andExpect(model().attribute("items", contains(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("count", is(2))
                        )
                )));
    }

    @Test
    void buyItem() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("cart", new HashMap<Long, Integer>());
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        cart.put(1L, 2);
        mockMvc.perform(post("/buy")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/1?newOrder=true"));
        assertNull(((Map<Long, Integer>) session.getAttribute("cart")));
    }
}
