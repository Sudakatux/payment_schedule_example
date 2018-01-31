package com.whiteprompt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

public class SheduleModel {
    private BigDecimal payment;
    private Month paymentMonth;
    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal RemainingPrincipal;

    public SheduleModel(BigDecimal payment, Month paymentMonth, BigDecimal principal, BigDecimal interest, BigDecimal remainingPrincipal) {
        this.payment = payment;
        this.paymentMonth = paymentMonth;
        this.principal = principal;
        this.interest = interest;
        RemainingPrincipal = remainingPrincipal;
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
        return RemainingPrincipal;
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
