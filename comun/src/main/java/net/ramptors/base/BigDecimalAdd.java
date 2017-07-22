package net.ramptors.base;

import java.math.BigDecimal;

import java8.util.function.BinaryOperator;

public class BigDecimalAdd implements BinaryOperator<BigDecimal> {
  @Override public BigDecimal apply(BigDecimal b, BigDecimal u) {
    return b.add(u);
  }
}
