package com.trusticket.trusticketbooking.service;

import com.trusticket.trusticketbooking.domain.Booking;
import com.trusticket.trusticketbooking.dto.BookingResponse;
import com.trusticket.trusticketbooking.repository.BookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    @Scheduled(cron = "0 0 0 * * *")
    public void cancelConfirm() {
        bookingRepository.updateStatus("CONFIRM", "CANCEL");
    }

    public List<BookingResponse> searchEventsByMemberId(Long memberId) {
        List<BookingResponse> bookings = bookingRepository.findBookingsByMemberId(memberId)
                .stream()
                .map(x -> BookingResponse.builder()
                            .bookingId(x.getBookingId())
                            .eventId(x.getEventId())
                            .paymentId(x.getPaymentId())
                            .memberId(x.getMemberId())
                            .status(x.getStatus()
                            ).build()
                )
                .collect(Collectors.toList());

        return bookings;
    }

    public BookingResponse searchEventById(Long memberId) {
        Optional<Booking> booking = bookingRepository.findById(memberId);

        if(!booking.isPresent()){
            return null;
        }
        else{
            Booking b = booking.get();
            BookingResponse response = BookingResponse.builder()
                    .bookingId(b.getBookingId())
                    .eventId(b.getEventId())
                    .paymentId(b.getPaymentId())
                    .memberId(b.getMemberId())
                    .status(b.getStatus()).build();

            return response;
        }

    }
}
