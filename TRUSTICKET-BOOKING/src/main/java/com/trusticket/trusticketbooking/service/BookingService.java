package com.trusticket.trusticketbooking.service;

import com.trusticket.trusticketbooking.repository.BookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    @Scheduled(cron = "0 0 0 * * *")
    public void cancelConfirm() {
        bookingRepository.updateStatus("CONFIRM", "CANCEL");
    }
}
