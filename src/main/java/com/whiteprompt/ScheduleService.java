package com.whiteprompt;

import com.whiteprompt.model.FuturePayment;
import com.whiteprompt.model.SheduleModel;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ScheduleService {
    final LocalDate closingDate;
    final LocalDate startDate;
    final BigDecimal yearlyInterest;
    final BigDecimal loanAmount;
    final List<FuturePayment> futurePaymentList;
    final BigDecimal interestPercent;
    final BigDecimal monthlyInterestPercent;
    final int term = 24;

    public ScheduleService(LocalDate closingDate, LocalDate startDate, int interestParam, int loanAmountParam, List<FuturePayment> futurePaymentList) {
        this.closingDate = closingDate;
        this.startDate = startDate;
        this.yearlyInterest = new BigDecimal(interestParam);
        this.loanAmount = new BigDecimal(loanAmountParam);
        this.futurePaymentList = futurePaymentList;

        interestPercent = yearlyInterest.divide(new BigDecimal(100));
        monthlyInterestPercent = interestPercent.divide(new BigDecimal(12));
    }

    public List<SheduleModel> createShedule(){

        final BigDecimal dailyInterestForGivenYear = yearlyInterest
                .divide(new BigDecimal(365), new MathContext(6, RoundingMode.HALF_DOWN));


        List<SheduleModel> shedule = new ArrayList<SheduleModel>();
        // Add days of only interest
        shedule.add(new SheduleModel(
                BigDecimal.ZERO,
                closingDate.getMonth(),
                BigDecimal.ZERO,
                dailyInterestForGivenYear
                        .divide(new BigDecimal(100), new MathContext(6, RoundingMode.HALF_DOWN))
                        .multiply(new BigDecimal(ChronoUnit.DAYS.between(closingDate, startDate)))
                        .multiply(loanAmount)
                ,
                loanAmount));

        // mutator to build up shedule
        LocalDate startingIn = startDate;



        // PMT =
        //monthlyPayment = (totalAmount*periodicInterestRate)/(1-(1+periodicInterestRate)^-term)
//        final BigDecimal totalAmount = loanAmount.multiply(
//                interestPercent
//                .add(new BigDecimal(1)))
//                .multiply(interestPercent.add(new BigDecimal(1)));

        //final BigDecimal payment = totalAmount.divide(new BigDecimal(term), RoundingMode.HALF_DOWN);
//        final BigDecimal payment = loanAmount.multiply(monthlyInterestPercent).divide(
//                new BigDecimal(1)
//                        .subtract(new BigDecimal(1)
//                                .add(monthlyInterestPercent)
//                                .pow(- term, new MathContext(6, RoundingMode.HALF_DOWN))),
//                RoundingMode.HALF_DOWN
//        );

//        final BigDecimal fixedInterestPerMonth = totalAmount
//                .subtract(loanAmount)
//                .divide(new BigDecimal(term), RoundingMode.HALF_DOWN);

        for(int i=0; i < term; i++){
            final BigDecimal lastRemainingPrincipal = shedule
                    .stream()
                    .map(SheduleModel::getRemainingPrincipal)
                    .reduce((__, second) -> second).orElse(loanAmount);
            final BigDecimal currentInterest = monthlyInterestPercent.multiply(lastRemainingPrincipal);
            final LocalDate paymentDate = startingIn.plusMonths(i);
           // final BigDecimal payment = calculatePMT(term-i,lastRemainingPrincipal);
            final BigDecimal payment = getPayment(paymentDate,term-i, lastRemainingPrincipal);
            final BigDecimal currentPrincipal = payment.subtract(currentInterest);

            shedule.add(new SheduleModel(
                    payment,
                    paymentDate.getMonth(),
                    currentPrincipal,
                    currentInterest,
                    lastRemainingPrincipal.subtract(currentPrincipal)));

        }

        return shedule;
    }

    private BigDecimal getPayment(final LocalDate paymentDate, int leftTerms,  BigDecimal lastRemainingPrincipal){
       return futurePaymentList
                .stream()
                .filter(fp-> fp.getFutureDate().getYear() == paymentDate.getYear() &&
                        fp.getFutureDate().getMonth()== paymentDate.getMonth())
                .map(FuturePayment::getFuturePayment)
                .findFirst()
                .orElse(calculatePMT(leftTerms,lastRemainingPrincipal));

    }

    private BigDecimal calculatePMT(int leftTerms, BigDecimal lastRemainingPrincipal){
        return lastRemainingPrincipal.multiply(monthlyInterestPercent).divide(
                new BigDecimal(1)
                        .subtract(new BigDecimal(1)
                                .add(monthlyInterestPercent)
                                .pow(- leftTerms, new MathContext(6, RoundingMode.HALF_DOWN))),
                RoundingMode.HALF_DOWN
        );
    }

}
