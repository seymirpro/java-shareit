package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    @Query("select r from ItemRequest r " +
            "join fetch r.requestor u " +
            "left join r.items i " +
            "where u = :user ")
    List<ItemRequest> findUserItemRequestsWithItemOptions(User user, Sort sort);

    @Query(value = "select r from ItemRequest r " +
            "join fetch r.requestor u " +
            "left join fetch r.items i " +
            "where u != :user",
            countQuery = "select r from ItemRequest r " +
                    "where r.requestor != :user")
    List<ItemRequest> getAllRequestsExcludingUser(User user, Pageable pageable);
}
