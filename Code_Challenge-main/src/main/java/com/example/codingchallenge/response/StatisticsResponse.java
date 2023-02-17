package com.example.codingchallenge.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponse implements Serializable {

  private BigDecimal min = new BigDecimal("0.00");
  private BigDecimal max = new BigDecimal("0.00");
  private BigDecimal avg = new BigDecimal("0.00");
  private BigDecimal sum = new BigDecimal("0.00");
  private long count = 0;

}
