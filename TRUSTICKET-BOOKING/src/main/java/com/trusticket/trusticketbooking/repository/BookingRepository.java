package com.trusticket.trusticketbooking.repository;

import com.trusticket.trusticketbooking.domain.Booking;
import com.trusticket.trusticketbooking.domain.Event;
import com.trusticket.trusticketbooking.dto.BookingStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status != 'CANCELED'")
    long countByStatusConfirmWithLock();

    boolean existsBookingByEventIdAndMemberId(String eventId, Long memberId);

    @Modifying
    @Transactional
    @Query("UPDATE Booking b SET b.status = :newStatus WHERE b.status = :oldStatus")
    int updateStatus(@Param("oldStatus") String oldStatus, @Param("newStatus") String newStatus);

    List<Booking> findBookingsByMemberId(Long memberId);

}