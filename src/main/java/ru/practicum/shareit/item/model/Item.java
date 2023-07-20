package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Builder
@Entity
@Table(name = "items")
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter @Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "available", nullable = false)
    private Boolean available;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @Transient
    private final ItemRequest request;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private List<Comment> comments;
}
