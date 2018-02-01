package com.whiteprompt;

import com.whiteprompt.model.FuturePayment;
import com.whiteprompt.model.SheduleModel;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScheduleService {
    private final LocalDate closingDate;
    private final LocalDate startDate;
    private final BigDecimal yearlyInterest;
    private final BigDecimal loanAmount;
    private final List<FuturePayment> futurePaymentList;
    private final BigDecimal interestPercent;
    private final BigDecimal monthlyInterestPercent;

    private static final int term = 24;
    private static final MathContext mathContext =  new MathContext(6, RoundingMode.HALF_DOWN);

    public ScheduleService(LocalDate closingDate, LocalDate startDate, int interestParam, int loanAmountParam, List<FuturePayment> futurePaymentList) {
        this.closingDate = closingDate;
        this.startDate = startDate;
        this.yearlyInterest = new BigDecimal(interestParam);
        this.loanAmount = new BigDecimal(loanAmountParam);
        this.futurePaymentList = futurePaymentList;

        interestPercent = yearlyInterest.divide(new BigDecimal(100));
        monthlyInterestPercent = interestPercent.divide(new BigDecimal(12));
    }

    /**
     * Creates the shedule for the given model
     * @return
     */
    public List<SheduleModel> createShedule(){

        final BigDecimal dailyInterestForGivenYear = yearlyInterest
                .divide(new BigDecimal(365), mathContext);

        // If closing date is before start date. calculate the interest in days, else return an empty array
        List<SheduleModel> shedule = closingDate.isBefore(startDate) ? new ArrayList<SheduleModel>(Arrays.asList(new SheduleModel(
                BigDecimal.ZERO,
                closingDate.getMonth(),
                BigDecimal.ZERO,
                dailyInterestForGivenYear
                        .divide(new BigDecimal(100), mathContext)
                        .multiply(new BigDecimal(ChronoUnit.DAYS.between(closingDate, startDate)))
                        .multiply(loanAmount)
                ,
                loanAmount))) : new ArrayList<SheduleModel>();



        for(int i=0; i < term; i++){ // iterate over the terms Generating the payment chedule
            final BigDecimal lastRemainingPrincipal = shedule
                    .stream()
                    .map(SheduleModel::getRemainingPrincipal)
                    .reduce((__, second) -> second).orElse(loanAmount);
            final BigDecimal currentInterest = monthlyInterestPercent.multiply(lastRemainingPrincipal);
            final LocalDate paymentDate = startDate.plusMonths(i);
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

    /**
     * Will return the payment for the given month
     * @param paymentDate date the payment will take place
     * @param leftTerms Remianing term to pay
     * @param lastRemainingPrincipal remianing principle from previous payment
     * @return Payment for that given month
     */
    private BigDecimal getPayment(final LocalDate paymentDate, int leftTerms,  BigDecimal lastRemainingPrincipal){
       return futurePaymentList
                .stream()
                .filter(fp-> fp.getFutureDate().getYear() == paymentDate.getYear() &&
                        fp.getFutureDate().getMonth()== paymentDate.getMonth())
                .map(FuturePayment::getFuturePayment)
                .findFirst()
                .orElse(calculatePMT(leftTerms,lastRemainingPrincipal));

    }

    /**
     * Calculates PMT formula https://en.wikipedia.org/wiki/Fixed-rate_mortgage
     * @param leftTerms terms left for payment
     * @param lastRemainingPrincipal Remaining payment
     * @return Payment
     */
    private BigDecimal calculatePMT(int leftTerms, BigDecimal lastRemainingPrincipal){
        return lastRemainingPrincipal.multiply(monthlyInterestPercent).divide(
                new BigDecimal(1)
                        .subtract(new BigDecimal(1)
                                .add(monthlyInterestPercent)
                                .pow(- leftTerms, mathContext)),
                RoundingMode.HALF_DOWN
        );
    }

}
