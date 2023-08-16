package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestCreateUpdateDto;
import ru.practicum.shareit.request.dto.ItemRequestGetDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemRequestService itemRequestService;
    private static final String REQ_USER_ID_HEADER = "X-Sharer-User-Id";

    @Test
    @SneakyThrows
    void testAddItemRequest() {
        ItemRequestGetDto itemRequestGetDto = ItemRequestGetDto.builder()
                .id(1)
                .requestId(2L)
                .created(LocalDateTime.now())
                .items(null)
                .build();
        ItemRequestCreateUpdateDto itemRequestCreateUpdateDto = ItemRequestCreateUpdateDto
                .builder()
                .description("description")
                .build();
        when(itemRequestService.addItemRequest(anyLong(), any(ItemRequestCreateUpdateDto.class)))
                .thenReturn(
                        itemRequestGetDto
                );
        long userId = 1L;
        mockMvc.perform(
                        post("/requests")
                                .header(REQ_USER_ID_HEADER, userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(itemRequestCreateUpdateDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestGetDto.getId()));
    }

    @Test
    @SneakyThrows
    void testGetItemRequests() {
        List<ItemRequestGetDto> itemRequestGetDtos = List.of(ItemRequestGetDto.builder()
                .id(1)
                .requestId(2L)
                .created(LocalDateTime.now())
                .items(null)
                .build());
        when(itemRequestService.getItemRequests(anyLong()))
                .thenReturn(itemRequestGetDtos);

        mockMvc.perform(get("/requests")
                        .header(REQ_USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        verify(itemRequestService, times(1)).getItemRequests(anyLong());
    }

    @Test
    @SneakyThrows
    void testGetAllRequestsExcludingUser() {
        List<ItemRequestGetDto> itemRequestGetDtos = List.of(ItemRequestGetDto.builder()
                .build());
        when(itemRequestService.getAllRequestsExcludingUser(anyLong(), anyInt(), anyInt()))
                .thenReturn(itemRequestGetDtos);
        mockMvc.perform(get("/requests/all")
                        .queryParam("from", String.valueOf(2))
                        .queryParam("size", String.valueOf(3))
                        .header(REQ_USER_ID_HEADER, 1L)
                )
                .andExpect(status().isOk());
        verify(itemRequestService, times(1)).getAllRequestsExcludingUser(anyLong(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void testGetItemRequestByID() {
        ItemRequestGetDto itemRequestGetDto = ItemRequestGetDto.builder()
                .id(1)
                .requestId(2L)
                .created(LocalDateTime.now())
                .items(null)
                .build();
        when(itemRequestService.getItemRequestByID(anyLong(), anyLong()))
                .thenReturn(itemRequestGetDto);
        long requestId = 2L;
        mockMvc.perform(get("/requests/{requestId}", requestId)
                .header(REQ_USER_ID_HEADER, 2)
        ).andExpect(status().isOk());
    }
}