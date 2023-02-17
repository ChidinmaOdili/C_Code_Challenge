package com.example.codingchallenge.service;

import com.example.codingchallenge.requests.TransactionRequest;
import com.example.codingchallenge.response.StatisticsResponse;

import java.util.List;

public interface TransactionService {

    void saveTransaction(TransactionRequest transactionRequest);

    StatisticsResponse getStatistics();

    List<TransactionRequest> fetchAllTransactionList();

    void deleteTransactions();
}
