package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Entity
@Table(name = "item_requests")
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;
    @ManyToOne
    @JoinColumn(name = "requestor_id")
    private User requestor;

    @Column(name = "created")
    private LocalDateTime created;

    @OneToMany(mappedBy = "request")
    @ToString.Exclude
    private Set<Item> items = new HashSet<>();
}
