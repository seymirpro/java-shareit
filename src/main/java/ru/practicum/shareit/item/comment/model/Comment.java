package ru.practicum.shareit.item.comment.model;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
@Getter @Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "item_id")
    private long itemId;

    @Column(name = "author_id")
    private long authorId;

    @Column(name = "created")
    private LocalDateTime created;

    @PrePersist
    public void prePersist() {
        created = LocalDateTime.now();
    }
}
