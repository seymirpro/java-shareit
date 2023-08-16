package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
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

    public static final String REQ_HEADER_USER_ID = "X-Sharer-User-Id";

    @SneakyThrows
    @Test
    void testCreateItemWithoutUserHeader() {
        Item item = new Item();
        Long userId = 1L;
        when(itemService.createItem(userId, ItemMapper.toItemDto(item)))
                .thenReturn(ItemMapper.toItemDto(item));

        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).createItem(userId, ItemMapper.toItemDto(item));
    }

    @Test
    void shouldCreateItem() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("item")
                .description("description")
                .requestId(1L)
                .available(true)
                .build();
        when(itemService.createItem(anyLong(), any(ItemDto.class)))
                .thenReturn(itemDto);

        String jsonItem = objectMapper.writeValueAsString(itemDto);

        mockMvc.perform(post("/items")
                        .header(REQ_HEADER_USER_ID, "1")
                        .content(jsonItem)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()));
        verify(itemService, times(1))
                .createItem(anyLong(), any(ItemDto.class));
    }

    @SneakyThrows
    @Test
    void getItem() {
        long itemId = 1L;
        long userId = 1L;
        mockMvc.perform(get("/items/{itemId}", itemId, userId))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getItems() {
        long userId = 1L;
        mockMvc.perform(get("/items", userId))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getItemsForSearch() {
        mockMvc.perform(get("/items/search"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetExceptionWithUpdateItemWithoutAvailable() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("item")
                .description("description")
                .requestId(1L)
                .build();
        when(itemService.updateItem(anyLong(), anyInt(), any(ItemDto.class)))
                .thenReturn(itemDto);

        String jsonItem = objectMapper.writeValueAsString(itemDto);

        mockMvc.perform(patch("/items/1")
                        .header(REQ_HEADER_USER_ID, "1")
                        .content(jsonItem)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()));
        verify(itemService, times(1)).updateItem(anyLong(), anyInt(), any(ItemDto.class));
    }

    @Test
    void shouldUpdateItem() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("item")
                .description("description")
                .requestId(1L)
                .available(true)
                .build();
        when(itemService.updateItem(anyLong(), anyInt(), any(ItemDto.class)))
                .thenReturn(itemDto);

        String jsonItem = objectMapper.writeValueAsString(itemDto);

        mockMvc.perform(patch("/items/1")
                        .header(REQ_HEADER_USER_ID, "1")
                        .content(jsonItem)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()));
        verify(itemService, times(1))
                .updateItem(anyLong(), anyInt(), any(ItemDto.class));
    }

    @Test
    void shouldGetExceptionWithGetAllByUserIdWithoutHeader() throws Exception {
        when(itemService.getAllUserItems(anyLong()))
                .thenReturn(List.of());
        mockMvc.perform(get("/items")
                        .header(REQ_HEADER_USER_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(itemService, times(1)).getAllUserItems(anyLong());
    }

    @Test
    @SneakyThrows
    void testGetItemDto() {
        long userId = 1;
        long itemId = 2;
        ItemOwnerDto itemOwnerDto = ItemOwnerDto.builder()
                .id(2L)
                .name("name")
                .description("description")
                .available(true)
                .requestId(1L)
                .lastBooking(null)
                .nextBooking(null)
                .comments(new ArrayList<>())
                .build();
        when(itemService.getItemByID(userId, itemId)).thenReturn(itemOwnerDto);
        String jsonItem = objectMapper.writeValueAsString(itemOwnerDto);
        mockMvc.perform(get("/items/{id}", itemId)
                        .header(REQ_HEADER_USER_ID, String.valueOf(userId))
                        .content(jsonItem)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemOwnerDto.getId()))
                .andExpect(jsonPath("$.name").value(itemOwnerDto.getName()))
                .andExpect(jsonPath("$.description").value(itemOwnerDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemOwnerDto.getAvailable()))
                .andExpect(jsonPath("$.requestId").value(itemOwnerDto.getRequestId()))
                .andExpect(jsonPath("$.lastBooking").value(itemOwnerDto.getLastBooking()))
                .andExpect(jsonPath("$.nextBooking").value(itemOwnerDto.getNextBooking()))
                .andExpect(jsonPath("$.comments").value(itemOwnerDto.getComments()));
    }

    @Test
    @SneakyThrows
    void testGetAllUserItems() {
        List<ItemOwnerDto> itemOwnerDtos = new ArrayList<>();
        itemOwnerDtos.add(
                ItemOwnerDto.builder().id(2L).name("name").description("description").available(true)
                        .requestId(1L).lastBooking(null).nextBooking(null).comments(new ArrayList<>())
                        .build()
        );
        when(itemService.getAllUserItems(anyLong())).thenAnswer(
                invocationOnMock -> itemOwnerDtos
        );
        String jsonString = objectMapper.writeValueAsString(itemOwnerDtos);
        mockMvc.perform(get("/users")
                .header(REQ_HEADER_USER_ID, 1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString)
        ).andExpect(status().isNotFound());
    }

    @Test
    void shouldGetExceptionWithSearchWithoutHeader() throws Exception {
        mockMvc.perform(get("/items/search")
                        .header(REQ_HEADER_USER_ID, 1)
                        .param("text", "text")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(itemService, times(1)).getItemsBySearchKeywords(anyString());
    }

    @Test
    void shouldGetExceptionWithCommentWithoutHeader() throws Exception {
        long itemId = 123;
        CommentDto commentDto = CommentDto.builder()
                .itemId(1L)
                .text("TEXT")
                .authorId(2L)
                .build();
        String jsonItem = objectMapper.writeValueAsString(commentDto);
        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header(REQ_HEADER_USER_ID, 1)
                        .content(jsonItem)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(itemService, times(1))
                .addComment(anyLong(), anyLong(), any(CommentDto.class));
    }
}