package com.whiteprompt.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FuturePayment {
    private LocalDate futureDate;
    private BigDecimal futurePayment;

    public FuturePayment(LocalDate futureDate, BigDecimal futurePayment) {
        this.futureDate = futureDate;
        this.futurePayment = futurePayment;
    }

    public LocalDate getFutureDate() {
        return futureDate;
    }

    public BigDecimal getFuturePayment() {
        return futurePayment;
    }
}
