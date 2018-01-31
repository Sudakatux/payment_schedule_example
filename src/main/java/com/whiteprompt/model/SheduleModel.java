package com.whiteprompt.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;

public class SheduleModel {
    private BigDecimal payment;
    private Month paymentMonth;
    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal remainingPrincipal;

    private static final MathContext mathContext = new MathContext(6, RoundingMode.HALF_DOWN);

    public SheduleModel(BigDecimal payment, Month paymentMonth, BigDecimal principal, BigDecimal interest, BigDecimal remainingPrincipal) {
        this.payment = payment.round(mathContext);
        this.paymentMonth = paymentMonth;
        this.principal = principal.round(mathContext);
        this.interest = interest.round(mathContext);
        this.remainingPrincipal = remainingPrincipal.round(mathContext);
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public Month getPaymentMonth() {
        return paymentMonth;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public BigDecimal getRemainingPrincipal() {
        return remainingPrincipal;
    }

    @Override
    public String toString() {
        return "\n\npayment: "+payment+"\n" +
                "month: "+paymentMonth+"\n" +
                "principal: "+principal+"\n"+
                "interest: "+interest+"\n"+
                "remaining principal:"+getRemainingPrincipal();
    }
}
