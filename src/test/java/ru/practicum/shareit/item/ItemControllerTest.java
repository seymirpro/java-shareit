package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ItemControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemServiceImpl itemService;
    @MockBean
    private UserService userService;
    @MockBean
    private BookingService bookingService;
    @MockBean
    private ItemRequestService itemRequestService;
    @InjectMocks
    private ItemController itemController;

    @SneakyThrows
    @Test
    void add() {
        Item item = new Item();
        Long userId = 1L;
        when(itemService.createItem(userId, ItemMapper.toItemDto(item))).thenReturn(ItemMapper.toItemDto(item));

        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).createItem(userId, ItemMapper.toItemDto(item));
    }

    @SneakyThrows
    @Test
    void getItem() {
        long itemId = 1L;
        long userId = 1L;
        mockMvc.perform(get("/items/{itemId}", itemId, userId)).andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getItems() {
        long userId = 1L;
        mockMvc.perform(get("/items", userId)).andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getItemsForSearch() {
        mockMvc.perform(get("/items/search"))
                .andExpect(status().isBadRequest());
    }
}