package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;
import ru.practicum.shareit.booking.dto.BookingOwnerGetDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.comment.dto.CommentGetDto;
import ru.practicum.shareit.item.comment.model.Comment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
class ItemOwnerDtoTest {
    @Autowired
    private JacksonTester<ItemOwnerDto> json;

    private ItemOwnerDto itemOwnerDto;

    private Long id = 1L;
    private String name = "Test Item";
    private String description = "This is a test item";
    private Boolean available = true;
    private Long requestId = 2L;
    private BookingOwnerGetDto lastBooking;
    private BookingOwnerGetDto nextBooking;
    private List<CommentGetDto> comments;
    private ItemOwnerDto itemOwnerDtoT;

    @BeforeEach
    public void setUp() {
        itemOwnerDto = new ItemOwnerDto(id, name, description, available, requestId, lastBooking, nextBooking, comments);
        itemOwnerDtoT = ItemOwnerDto.builder().build();
    }

    @Test
    void testSerialize() throws IOException {
        ItemOwnerDto dto = ItemOwnerDto.builder()
                .id(1L)
                .name("itemName")
                .description("itemDescription")
                .available(false)
                .requestId(4L)
                .comments(new ArrayList<>())
                .nextBooking(null)
                .lastBooking(null)
                .build();

        JsonContent<ItemOwnerDto> result = json.write(dto);

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).hasJsonPathStringValue("$.name");
        assertThat(result).hasJsonPathStringValue("$.description");
        assertThat(result).hasJsonPathBooleanValue("$.available");
        assertThat(result).hasJsonPathNumberValue("$.requestId");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("itemName");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("itemDescription");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(false);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(4);
    }

    @Test
    void testDeserialize() throws IOException {
        String content = "{\"id\":\"1\",\"name\":\"itemName\",\"description\":\"itemDescription\"" +
                ",\"available\":\"false\"}";

        ObjectContent<ItemOwnerDto> result = json.parse(content);

        assertThat(result).isEqualTo(ItemOwnerDto.builder()
                .id(1L)
                .name("itemName")
                .description("itemDescription")
                .available(false)
                .comments(null)
                .nextBooking(null)
                .lastBooking(null)
                .build());
    }

    @Test
    public void testConstructorWithAllParameters() {
        Long id = 1L;
        String name = "Item 1";
        String description = "Description of item";
        boolean available = true;
        Long requestId = 100L;
        Booking lastBooking = new Booking();
        Booking nextBooking = new Booking();
        Collection<Comment> comments = new ArrayList<>();

        ItemOwnerDto itemOwnerDto = ItemOwnerDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .available(available)
                .requestId(requestId)
                .lastBooking(BookingOwnerGetDto.builder().build())
                .nextBooking(BookingOwnerGetDto.builder().build())
                .comments(List.of())
                .build();

        assertEquals(id, itemOwnerDto.getId());
        assertEquals(name, itemOwnerDto.getName());
        assertEquals(description, itemOwnerDto.getDescription());
        assertEquals(available, itemOwnerDto.getAvailable());
        assertEquals(requestId, itemOwnerDto.getRequestId());
        Assertions.assertNotNull(itemOwnerDto.getLastBooking());
        Assertions.assertNotNull(itemOwnerDto.getNextBooking());
        Assertions.assertNotNull(itemOwnerDto.getComments());
    }

    @Test
    public void testConstructorWithRequiredParameters() {
        Long id = 1L;
        String name = "Item 1";
        String description = "Description of item";
        Boolean available = true;
        Long requestId = 100L;
        Collection<Comment> comments = new ArrayList<>();

        ItemOwnerDto itemOwnerDto = new ItemOwnerDto(id, name, description, available, requestId, comments);

        assertEquals(id, itemOwnerDto.getId());
        assertEquals(name, itemOwnerDto.getName());
        assertEquals(description, itemOwnerDto.getDescription());
        assertEquals(available, itemOwnerDto.getAvailable());
        assertEquals(requestId, itemOwnerDto.getRequestId());
        Assertions.assertNull(itemOwnerDto.getLastBooking());
        Assertions.assertNull(itemOwnerDto.getNextBooking());
        assertEquals(comments, itemOwnerDto.getComments());
    }

    @Test
    public void testId() {
        Long id = 1L;
        itemOwnerDtoT.setId(id);
        assertEquals(id, itemOwnerDtoT.getId());
    }

    @Test
    public void testName() {
        String name = "Test Item";
        itemOwnerDtoT.setName(name);
        assertEquals(name, itemOwnerDtoT.getName());
    }

    @Test
    public void testDescription() {
        String description = "Test Description";
        itemOwnerDtoT.setDescription(description);
        assertEquals(description, itemOwnerDtoT.getDescription());
    }

    @Test
    public void testAvailable() {
        Boolean available = true;
        itemOwnerDtoT.setAvailable(available);
        assertEquals(available, itemOwnerDtoT.getAvailable());
    }

    @Test
    public void testRequestId() {
        Long requestId = 1L;
        itemOwnerDtoT.setRequestId(requestId);
        assertEquals(requestId, itemOwnerDtoT.getRequestId());
    }

    @Test
    public void testLastBooking() {
        BookingOwnerGetDto lastBooking = new BookingOwnerGetDto();
        itemOwnerDtoT.setLastBooking(lastBooking);
        assertEquals(lastBooking, itemOwnerDtoT.getLastBooking());
    }

    @Test
    public void testNextBooking() {
        BookingOwnerGetDto nextBooking = new BookingOwnerGetDto();
        itemOwnerDtoT.setNextBooking(nextBooking);
        assertEquals(nextBooking, itemOwnerDtoT.getNextBooking());
    }

    @Test
    public void testComments() {
        List<CommentGetDto> comments = new ArrayList<>();
        CommentGetDto comment1 = CommentGetDto.builder().build();
        CommentGetDto comment2 = CommentGetDto.builder().build();
        comments.add(comment1);
        comments.add(comment2);
        itemOwnerDtoT.setComments(comments);
        assertEquals(comments, itemOwnerDtoT.getComments());
    }
}