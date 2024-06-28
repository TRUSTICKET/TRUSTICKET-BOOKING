package com.trusticket.trusticketbooking.api;

import com.trusticket.trusticketbooking.common.CommonResponse;
import com.trusticket.trusticketbooking.domain.Booking;
import com.trusticket.trusticketbooking.dto.BookingResponse;
import com.trusticket.trusticketbooking.repository.BookingCacheRepository;
import com.trusticket.trusticketbooking.repository.BookingRepository;
import com.trusticket.trusticketbooking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/booking")
public class BookingController {
    private final BookingCacheRepository bookingCacheRepository;
    private final BookingService bookingService;


    @GetMapping(value = "status/{offset}")
    public CommonResponse<String> getBookingData(@PathVariable("offset") Long offset) {
        String result = bookingCacheRepository.getBookingCache(offset);
        return new CommonResponse<>(true, HttpStatus.OK, "결과를 받았습니다", result);
    }

    @GetMapping(value = "/member/{memberId}")
    public CommonResponse<List<BookingResponse>> queryMemberBookingData(@PathVariable("memberId") Long memberId) {
        List<BookingResponse> result = bookingService.searchEventsByMemberId(memberId);
        return new CommonResponse<>(true, HttpStatus.OK, "결과를 받았습니다", result);
    }

    @GetMapping(value = "/id/{bookingId}")
    public CommonResponse<BookingResponse> queryBookingDataById(@PathVariable("bookingId") Long bookingId) {
        BookingResponse result = bookingService.searchEventById(bookingId);
        if(result == null){
            return new CommonResponse<>(false, HttpStatus.OK, "해당 내역이 존재하지 않습니다.", result);
        }
        else{
            return new CommonResponse<>(true, HttpStatus.OK, "결과를 받았습니다", result);
        }

    }
}
