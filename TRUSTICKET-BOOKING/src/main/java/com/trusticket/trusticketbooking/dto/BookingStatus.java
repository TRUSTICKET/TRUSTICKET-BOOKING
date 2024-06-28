package com.trusticket.trusticketbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BookingStatus {
    String result;
    String message;
}
