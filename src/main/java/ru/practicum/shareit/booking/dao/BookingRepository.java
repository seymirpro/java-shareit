package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT e FROM Booking e  JOIN FETCH e.item JOIN FETCH e.booker" +
            " WHERE e.booker.id = :bookerId AND e.id = :bookingId" +
            " ORDER BY e.start DESC")
    Booking findByBookerIdAndBookingId(long bookerId, long bookingId);

    @Query("SELECT e FROM Booking e  JOIN FETCH e.item JOIN FETCH e.booker" +
            " WHERE e.item.owner.id = :itemOwnerId AND e.id = :bookingId" +
            " ORDER BY e.start DESC")
    Booking findByItemOwnerIdAndBookingId(long itemOwnerId, long bookingId);

    @Query("SELECT b  " +
            "FROM Booking b JOIN b.item JOIN b.booker " +
            "WHERE b.item.owner.id=:itemOwnerId " +
            "ORDER BY b.end DESC")
    List<Booking> findAllByItemOwnerId(long itemOwnerId);

    @Query("SELECT b  " +
            "FROM Booking b JOIN b.item JOIN b.booker WHERE 1=1 " +
            "AND b.item.owner.id=:itemOwnerId " +
            "AND b.end > CURRENT_TIMESTAMP() " +
            "ORDER BY b.end DESC")
    List<Booking> findFutureBookingsByItemOwnerId(long itemOwnerId);

    @Query("SELECT b " +
            "FROM Booking b JOIN b.item JOIN b.booker WHERE b.booker.id=:bookerId " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByBookerId(long bookerId);

    @Query("SELECT b " +
            "FROM Booking b JOIN b.item JOIN b.booker WHERE 1=1 " +
            "AND b.booker.id=:userId " +
            "AND CURRENT_TIMESTAMP() BETWEEN b.start AND b.end " +
            "ORDER BY b.start ASC")
    List<Booking> findCurrentBookingsByBookerId(Long userId);

    @Query("SELECT b " +
            "FROM Booking b JOIN b.item JOIN b.booker WHERE 1=1 " +
            "AND b.booker.id=:userId " +
            "AND b.end < CURRENT_TIMESTAMP()" +
            "ORDER BY b.start DESC")
    List<Booking> findPastBookingsByBookerId(Long userId);

    @Query("SELECT b " +
            "FROM Booking b JOIN b.item JOIN b.booker WHERE 1=1 " +
            "AND b.booker.id=:userId " +
            "AND b.status='WAITING'" +
            "ORDER BY b.start DESC")
    List<Booking> findWaitingBookingsByBookerId(Long userId);

    @Query("SELECT b " +
            "FROM Booking b JOIN b.item JOIN b.booker WHERE 1=1 " +
            "AND b.booker.id=:userId " +
            "AND b.status='REJECTED'" +
            "ORDER BY b.start DESC")
    List<Booking> findRejectedBookingsByBookerId(Long userId);

    @Query("SELECT b " +
            "FROM Booking b JOIN b.item JOIN b.booker WHERE 1=1 " +
            "AND b.booker.id=:userId " +
            "AND CURRENT_TIMESTAMP() < b.end  " +
            "ORDER BY b.end DESC")
    List<Booking> findFutureBookingsByBookerId(Long userId);

    @Query("SELECT b FROM Booking b JOIN b.item JOIN b.booker " +
            "WHERE 1=1 " +
            "AND b.item.id=:itemId " +
            "AND b.item.owner.id=:userId " +
            "AND b.status='APPROVED' " +
            "AND b.start < CURRENT_TIMESTAMP() " +
            "ORDER BY b.start DESC")
    List<Booking> findLastBookingByItemOwnerId(long userId, long itemId);

    @Query("SELECT b FROM Booking  b JOIN b.item JOIN b.booker " +
            "WHERE 1=1 " +
            "AND b.item.id=:itemId " +
            "AND b.item.owner.id=:userId " +
            "AND b.status='APPROVED' " +
            "AND b.start > CURRENT_TIMESTAMP()" +
            "ORDER BY b.start ASC")
    List<Booking> findNextBookingByItemOwnerId(long userId, long itemId);

    @Query("SELECT b FROM Booking b JOIN b.item JOIN b.booker " +
            "WHERE 1=1 " +
            "AND b.item.owner.id=:itemOwnerId " +
            "AND b.status='REJECTED'")
    List<Booking> findRejectedBookingsByItemOwnerId(long itemOwnerId);

    @Query("SELECT b FROM Booking b JOIN b.item JOIN b.booker " +
            "WHERE 1=1 " +
            "AND b.item.owner.id=:itemOwnerId " +
            "AND b.status='WAITING'")
    List<Booking> findWaitingBookingsByItemOwnerId(long itemOwnerId);

    List<Booking> findBookingByItemIdAndStatusNotInAndStartBefore(long itemId, List<Status> rejected, LocalDateTime now);

    @Query("SELECT b " +
            "FROM Booking b JOIN b.item JOIN b.booker WHERE 1=1 " +
            "AND b.item.owner.id=:itemOwnerId " +
            "AND CURRENT_TIMESTAMP() BETWEEN b.start AND b.end " +
            "ORDER BY b.start ASC")
    List<Booking> findCurrentBookingsByItemOwnerId(long itemOwnerId);

    @Query("SELECT b " +
            "FROM Booking b JOIN b.item JOIN b.booker WHERE 1=1 " +
            "AND b.item.owner.id=:itemOwnerId " +
            "AND b.end < CURRENT_TIMESTAMP()" +
            "ORDER BY b.start DESC")
    List<Booking> findPastBookingsByItemOwnerId(long itemOwnerId);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id=:ownerId")
    List<Booking> findBookingsByItemOwnerId(Long ownerId);
}
