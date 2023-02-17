package com.example.codingchallenge.helper;

import com.example.codingchallenge.requests.TransactionRequest;
import com.example.codingchallenge.response.StatisticsResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class TestHelper {

    private TestHelper() {
    }

    public static StatisticsResponse mockResponse(){
        return StatisticsResponse.builder()
                .max(new BigDecimal("10.50"))
                .min(new BigDecimal("2.33"))
                .avg(new BigDecimal("9.00"))
                .sum(new BigDecimal("27.00"))
                .count(3).build();
    }

    public static TransactionRequest formRequest(String amount, LocalDateTime dateTime){
        return TransactionRequest.builder()
                .timestamp(dateTime)
                .amount(new BigDecimal(amount)).build();
    }

}
