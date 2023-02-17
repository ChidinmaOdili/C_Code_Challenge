package com.example.codingchallenge.requests;
import com.example.codingchallenge.helper.LocalDateTimeDeserializer;
import com.example.codingchallenge.helper.LocalDateTimeSerializer;
import com.example.codingchallenge.validator.DateValidator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {

    @NotNull(message = "Amount Cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Value has to be greater than 0.0")
    @Digits(integer = Integer.MAX_VALUE, fraction = 4, message = "Ensure that the fraction is at most 4 decimal place")
    private BigDecimal amount;

    @NotNull(message = "Date Cannot be null")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @DateValidator
    private LocalDateTime timestamp;

}
