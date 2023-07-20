package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.comment.CommentBadRequestException;
import ru.practicum.shareit.exception.item.ItemDoesNotExistException;
import ru.practicum.shareit.exception.item.NotItemOwnerException;
import ru.practicum.shareit.exception.user.UserDoesNotExistException;
import ru.practicum.shareit.item.comment.dao.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentGetDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl {

    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private BookingRepository bookingRepository;
    private CommentRepository commentRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository,
                           UserRepository userRepository,
                           BookingRepository bookingRepository,
                           CommentRepository commentRepository
    ) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    public ItemDto createItem(long id, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        User user = userRepository.findById(id).orElseThrow(UserDoesNotExistException::new);
        item.setOwner(user);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    public ItemDto updateItem(long userId, int itemId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserDoesNotExistException::new);
        Item item = ItemMapper.toItem(itemDto);
        item.setId(itemId);
        item.setOwner(user);

        Item itemFromDB = itemRepository.findById(item.getId())
                .orElseThrow(ItemDoesNotExistException::new);

        if (itemFromDB.getOwner().getId() != item.getOwner().getId()) {
            throw new NotItemOwnerException();
        }

        if (item.getAvailable() != null && !item.getAvailable().equals(itemFromDB.getAvailable())) {
            itemFromDB.setAvailable(item.getAvailable());
        }

        if (item.getName() != null && !item.getName().equals(itemFromDB.getName())) {
            itemFromDB.setName(item.getName());
        }

        if (item.getDescription() != null &&
                !item.getDescription().equals(itemFromDB.getDescription())) {
            itemFromDB.setDescription(item.getDescription());
        }

        return ItemMapper.toItemDto(itemRepository.save(itemFromDB));
    }

    public ItemOwnerDto getItemByID(long userId, long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserDoesNotExistException::new);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(ItemDoesNotExistException::new);
        Booking bookingLast = null;
        Booking bookingNext = null;

        if (item.getOwner().getId() == userId) {
            List<Booking> bookingNextList = bookingRepository.findNextBookingByItemOwnerId(userId, itemId);

            Optional<Booking> next = bookingNextList.stream().findFirst();
            if (next.isPresent()) {
                bookingNext = next.get();
            }

            List<Booking> bookingLastList = bookingRepository.findLastBookingByItemOwnerId(userId, itemId);
            Optional<Booking> last = bookingLastList.stream().findFirst();
            if (last.isPresent()) {
                bookingLast = last.get();
            }

        }

        ItemOwnerDto itemOwnerDto = ItemMapper.itemOwnerDto(item, bookingLast, bookingNext);
        return itemOwnerDto;
    }

    public List<ItemOwnerDto> getAllUserItems(Long ownerId) {
        List<Item> userItems = itemRepository.findByOwnerId(ownerId);

        List<ItemOwnerDto> itemOwnerDtos = userItems.stream()
                .map(i -> {
                    List<Booking> bookingLastList = bookingRepository.findLastBookingByItemOwnerId(ownerId, i.getId());
                    List<Booking> bookingNextList = bookingRepository.findNextBookingByItemOwnerId(ownerId, i.getId());
                    ItemOwnerDto itemOwnerDto = ItemMapper.itemOwnerDto(i,
                            bookingLastList.size() > 0 ? bookingLastList.get(0) : null,
                            bookingNextList.size() > 0 ? bookingNextList.get(0) : null

                    );
                    return itemOwnerDto;
                }).collect(Collectors.toList());
        return itemOwnerDtos;
    }

    public List<ItemDto> getItemsBySearchKeywords(String searchText) {
        return ItemMapper
                .toItemDtoList(
                        itemRepository.findItemsByDescriptionContainingIgnoreCase(searchText)
                );
    }

    public CommentGetDto addComment(Long ownerId, Long itemId, CommentDto commentDto) {
        if (itemId == null || commentDto.getText() == null || commentDto.getText().isEmpty()) {
            throw new CommentBadRequestException();
        }
        Item item = itemRepository.findByOwnerIdAndId(ownerId, itemId);
        commentDto.setItemId(itemId);
        commentDto.setAuthorId(ownerId);
        List<Booking> bookings = bookingRepository.findBookingByItemIdAndStatusNotInAndStartBefore(itemId,
                List.of(Status.REJECTED), LocalDateTime.now());
        if (bookings == null || bookings.isEmpty()) {
            throw new CommentBadRequestException();
        }
        User author = userRepository.findById(ownerId).orElseThrow(UserDoesNotExistException::new);
        Comment newComment = CommentMapper.toComment(commentDto);
        Comment commentInserted = commentRepository.save(newComment);
        CommentGetDto commentGetDto = CommentMapper.toCommentGetDto(commentInserted);
        commentGetDto.setAuthorName(author.getName());
        return commentGetDto;
    }
}
