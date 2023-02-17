package com.example.codingchallenge.serviceimpl;
import com.example.codingchallenge.requests.TransactionRequest;
import com.example.codingchallenge.response.StatisticsResponse;
import com.example.codingchallenge.service.TransactionService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final List<TransactionRequest> transactionRequestList = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void saveTransaction(TransactionRequest transactionRequest) {
        //Add request to in-memory List
        transactionRequestList.add(transactionRequest);
    }

    @Override
    public StatisticsResponse getStatistics() {
        if(transactionRequestList.isEmpty())
            return new StatisticsResponse();

        BigDecimal max = new BigDecimal("0.00");
        BigDecimal min = null;
        BigDecimal sum = new BigDecimal("0.00");
        long count = 0;

        synchronized (transactionRequestList) {
            Iterator i = transactionRequestList.iterator(); // Must be in synchronized block
            while (i.hasNext()) {
                TransactionRequest tran = (TransactionRequest) i.next();
                if(tran.getTimestamp().isBefore(LocalDateTime.now().minusSeconds(30)))
                    continue;

                if(tran.getAmount().compareTo(max) > 0)
                    max = tran.getAmount();

                if(min == null || tran.getAmount().compareTo(min) < 0)
                    min = tran.getAmount();

                sum = sum.add(tran.getAmount());
                ++count;
            }
        }

        max = max.setScale(2, RoundingMode.HALF_UP);
        min = min.setScale(2, RoundingMode.HALF_UP);
        sum = sum.setScale(2, RoundingMode.HALF_UP);

        BigDecimal avg = count != 0 ? sum.divide(BigDecimal.valueOf(count), RoundingMode.HALF_UP) : new BigDecimal("0.00");
        return StatisticsResponse.builder()
                .max(max).min(min).avg(avg)
                .sum(sum).count(count).build();
    }

    @Override
    public List<TransactionRequest> fetchAllTransactionList() {
        return this.transactionRequestList;
    }

    @Override
    public void deleteTransactions() {
        this.transactionRequestList.clear();
    }
}
