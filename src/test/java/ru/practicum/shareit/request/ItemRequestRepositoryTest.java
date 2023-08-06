package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(2, "user 1", "user@mail.ru");
    }

    @Test
    void findUserItemRequestsWithItemOptions() {
        List<ItemRequest> itemRequests = itemRequestRepository.findUserItemRequestsWithItemOptions(user,
                Sort.by("created").ascending());
        assertTrue(!itemRequests.isEmpty());
        assertEquals(2, itemRequests.get(0).getRequestor().getId());
    }

    @Test
    void getAllRequestsExcludingUser() {
        List<ItemRequest> itemRequests = itemRequestRepository.getAllRequestsExcludingUser(user,
                Pageable.ofSize(2));
        assertTrue(itemRequests.isEmpty());
    }
}