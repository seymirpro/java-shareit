package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE 1=1 " +
            "AND LENGTH(:searchText)<>0 " +
            "AND i.available = true " +
            "AND LOWER(i.description) LIKE CONCAT('%', LOWER(:searchText), '%')")
    List<Item> findItemsByDescriptionContainingIgnoreCase(@Param("searchText") String searchText);

    List<Item> findByOwnerId(Long ownerId);

    Item findByOwnerIdAndId(Long ownerId, Long itemId);
}
