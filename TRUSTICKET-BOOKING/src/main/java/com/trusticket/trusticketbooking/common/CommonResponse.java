package com.trusticket.trusticketbooking.common;



import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;


@Data
@AllArgsConstructor
public class CommonResponse<T> {

    private final boolean success;

    private final HttpStatus status;

    private final String message;

    private final T response;


}
