package com.trusticket.trusticketbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BookingResponse {
    private Long bookingId;

    private String eventId;

    private Long memberId;

    private Long paymentId;

    private String status;


}
