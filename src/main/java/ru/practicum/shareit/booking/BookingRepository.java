package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking,Long> {


    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findByEndBeforeAndBookerIdAndStatusOrderByStartDesc(LocalDateTime now, Long bookerId, Status status);

    List<Booking> findByStartAfterAndBookerIdAndStatusOrderByStartDesc(LocalDateTime now, Long bookerId, Status status);

    List<Booking> findByStartBeforeAndEndAfterAndBookerIdAndStatusOrderByStartDesc(LocalDateTime now1,
                                                                                   LocalDateTime now2, Long bookerId, Status status);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, Status status);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.item i " +
            "JOIN FETCH i.owner o " +
            "WHERE o.id = :ownerId " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.item i " +
            "JOIN FETCH i.owner o " +
            "WHERE o.id = :ownerId " +
            "AND b.end < CURRENT_TIMESTAMP " +
            "AND b.status = ru.practicum.shareit.booking.Status.APPROVED " +
            "ORDER BY b.start DESC")
    List<Booking> findByOwnerIdStatePast(@Param("ownerId") Long ownerId);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.item i " +
            "JOIN FETCH i.owner o " +
            "WHERE o.id = :ownerId " +
            "AND b.start > CURRENT_TIMESTAMP " +
            "AND b.status = ru.practicum.shareit.booking.Status.APPROVED " +
            "ORDER BY b.start DESC")
    List<Booking> findByOwnerIdStateFuture(@Param("ownerId") Long ownerId);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.item i " +
            "JOIN FETCH i.owner o " +
            "WHERE o.id = :ownerId " +
            "AND b.start < CURRENT_TIMESTAMP " +
            "AND b.end > CURRENT_TIMESTAMP " +
            "AND b.status = ru.practicum.shareit.booking.Status.APPROVED " +
            "ORDER BY b.start DESC")
    List<Booking> findByOwnerIdStateCurrent(@Param("ownerId") Long ownerId);


    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.item i " +
            "JOIN FETCH i.owner o " +
            "WHERE o.id = :ownerId " +
            "AND b.status = ru.practicum.shareit.booking.Status.WAITING " +
            "ORDER BY b.start DESC")
    List<Booking> findByOwnerIdWaiting(@Param("ownerId") Long ownerId);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.item i " +
            "JOIN FETCH i.owner o " +
            "WHERE o.id = :ownerId " +
            "AND b.status = ru.practicum.shareit.booking.Status.REJECTED " +
            "ORDER BY b.start DESC")
    List<Booking> findByOwnerIdRejected(@Param("ownerId") Long ownerId);


    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.item i " +
            "JOIN FETCH i.owner o " +
            "WHERE o.id = :ownerId " +
            "AND b.end < CURRENT_TIMESTAMP " +
            "AND b.status = :status " +
            "ORDER BY b.start DESC ")
    List<Booking> findLast(@Param("ownerId") Long ownerId,
                           @Param("status") Status status,
                           Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.item i " +
            "JOIN FETCH i.owner o " +
            "WHERE o.id = :ownerId " +
            "AND b.start > CURRENT_TIMESTAMP " +
            "AND b.status = :status " +
            "ORDER BY b.start ASC")
    List<Booking> findNext(@Param("ownerId") Long ownerId,
                           @Param("status") Status status,
                           Pageable pageable);


    @Query("SELECT bk FROM Booking bk " +
            "JOIN FETCH bk.item i " +
            "JOIN FETCH bk.booker b " +
            "WHERE b.id = :bookerId " +
            "AND i.id = :itemId " +
            "AND bk.end < CURRENT_TIMESTAMP " +
            "AND bk.status = ru.practicum.shareit.booking.Status.APPROVED ")
    Optional<Booking> checkComments(@Param("bookerId") Long bookerId, @Param("itemId") Long itemId);


}
