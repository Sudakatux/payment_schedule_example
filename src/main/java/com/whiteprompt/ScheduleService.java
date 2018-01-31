package com.whiteprompt;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ScheduleService {
    public static final List<SheduleModel> createShedule(
            final LocalDate closingDate,
            final LocalDate startDate,
            final int interestParam
    ){

        final BigDecimal yearlyInterest = new BigDecimal(interestParam);
        final BigDecimal loanAmount = new BigDecimal(100000);


        int term = 24;

        final BigDecimal dailyInterestForGivenYear = yearlyInterest
                .divide(new BigDecimal(365), 2, RoundingMode.HALF_DOWN);


        List<SheduleModel> shedule = new ArrayList<SheduleModel>();
        // Add days of only interest
        shedule.add(new SheduleModel(
                BigDecimal.ZERO,
                closingDate.getMonth(),
                BigDecimal.ZERO,
                dailyInterestForGivenYear
                        .divide(new BigDecimal(100))
                        .multiply(new BigDecimal(ChronoUnit.DAYS.between(closingDate, startDate)))
                        .multiply(loanAmount)
                ,
                loanAmount));

        // mutator to build up shedule
        LocalDate startingIn = startDate;

        final BigDecimal interestPercent = yearlyInterest.divide(new BigDecimal(100));

        final BigDecimal totalAmount = loanAmount.multiply(
                interestPercent
                .add(new BigDecimal(1)))
                .multiply(interestPercent.add(new BigDecimal(1)));

        final BigDecimal payment = totalAmount.divide(new BigDecimal(term), RoundingMode.HALF_DOWN);

        final BigDecimal fixedInterestPerMonth = totalAmount
                .subtract(loanAmount)
                .divide(new BigDecimal(term),RoundingMode.HALF_DOWN);

        for(int i=0; i != term; i++){

            shedule.add(new SheduleModel(
                    payment,
                    startingIn.getMonth(),
                    payment.subtract(fixedInterestPerMonth),
                    fixedInterestPerMonth,
                    loanAmount
                            .subtract(
                                    shedule
                                            .stream()
                                            .map(SheduleModel::getPrincipal)
                                            .reduce(BigDecimal.ZERO,(x,y) -> x.add(y))
                                            .add(payment.subtract(fixedInterestPerMonth))).setScale(2, RoundingMode.HALF_UP)

                    ));
            startingIn = startingIn.plusMonths(1);
        }

        return shedule;
    }

}
