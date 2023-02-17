package com.example.codingchallenge.controller;
import com.example.codingchallenge.requests.TransactionRequest;
import com.example.codingchallenge.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static com.example.codingchallenge.helper.TestHelper.mockResponse;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    private final String URL_BASE = "/transaction";
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    TransactionControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void setUp() {
        doNothing().when(transactionService).saveTransaction(any());
        when(transactionService.getStatistics()).thenReturn(mockResponse());
        doNothing().when(transactionService).deleteTransactions();
    }

    @Test
    void should_successfully_post_transaction() throws Exception {
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .amount(new BigDecimal("12.3343"))
                .timestamp(LocalDateTime.now())
                .build();

       postTransaction(transactionRequest, status().isCreated());
    }

    @Test
    void whenTimestampIsStaleByAtLeast30Seconds_thenShouldReturnStatusNoContent() throws Exception {
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .amount(new BigDecimal("12.3343"))
                .timestamp(LocalDateTime.parse("2022-05-13T01:30:51.312Z", ISO_DATE_TIME ))
                .build();

        postTransaction(transactionRequest, status().isNoContent());
    }

    @Test
    void whenMoreThanFourFractionalDigits_thenShouldGiveConstraintViolations() throws Exception {
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .amount(new BigDecimal("12.33435"))
                .timestamp(LocalDateTime.parse("2022-05-13T00:45:51.312Z", ISO_DATE_TIME ))
                .build();

        postTransaction(transactionRequest, status().isUnprocessableEntity());
    }

    @Test
    void whenDateIsInFuture_thenShouldGiveConstraintViolations() throws Exception {
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .amount(new BigDecimal("12.35"))
                .timestamp(LocalDateTime.parse("2023-05-13T00:45:51.312Z", ISO_DATE_TIME ))
                .build();

        postTransaction(transactionRequest, status().isUnprocessableEntity());
    }

    @Test
    void whenStatisticsIsFetched_shouldReturnSuccess() throws Exception {

        mockMvc.perform(get(URL_BASE + "/statistics")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.max", Is.is(10.50)))
                .andExpect(jsonPath("$.min", Is.is(2.33)))
                .andExpect(jsonPath("$.avg", Is.is(9.00)))
                .andExpect(jsonPath("$.sum", Is.is(27.00)))
                .andExpect(jsonPath("$.count", Is.is(3)))
                .andDo(print());
    }

    @Test
    void whenDeleteIsInvoked_shouldReturnNoContentStatus() throws Exception {

        mockMvc.perform(delete(URL_BASE))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    private void postTransaction(TransactionRequest transactionRequest, ResultMatcher expectedStatus) throws Exception {

        mockMvc.perform(post(URL_BASE)
                        .contentType(APPLICATION_JSON).content(asJsonString(transactionRequest)))
                .andExpect(expectedStatus)
                .andDo(print());
    }



    private String asJsonString(final Object obj) {
        try {
            return this.objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}