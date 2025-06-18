package com.github.maximslepukhin.intershop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ItemControllerTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldShowItems() throws Exception {
        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(get("/main/items")
                        .param("search", "")
                        .param("sort", "NO")
                        .param("pageSize", "100")
                        .param("pageNumber", "1")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().attribute("search", ""))
                .andExpect(model().attribute("sort", "NO"))
                .andExpect(model().attribute("items", arrayWithSize(5)));

    }

    @Test
    void shouldShowItem() throws Exception {
        mockMvc.perform(get("/items/5")
                        .sessionAttr("cart", Map.of(5L, 2)))
                .andExpect(status().isOk())
                .andExpect(view().name("item"))
                .andExpect(model().attribute("item",
                        allOf(
                                hasProperty("id", is(5L)),
                                hasProperty("count", is(2))
                        )
                ));
    }
}

