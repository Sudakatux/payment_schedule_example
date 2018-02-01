package com.whiteprompt;

import com.whiteprompt.model.FuturePayment;
import de.vandermeer.asciitable.AsciiTable;

import javax.swing.text.DateFormatter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App 
{
    final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static void main( String[] args )
    {
        LocalDate startDate = LocalDate.of(2018, Month.JANUARY,1);
        LocalDate closingDate = LocalDate.of(2017, Month.DECEMBER, 15);
        List<FuturePayment> futurePayments = new ArrayList<FuturePayment>();
        int interest = 12;
        int loanAmount = 100000;

        if(args.length > 1 ){
            if(args[0].equals("--closeDateStartDateInterestLoanAmountSimulations")) {
                String closeDateArg = args[1];
                String startDateArg = args[2];
                String interestPercent = args[3];
                String loanAmountArg = args[4];
                String simulations = args[5];

                startDate = LocalDate.parse(startDateArg, dtf);
                closingDate = LocalDate.parse(closeDateArg, dtf);
                interest = Integer.valueOf(interestPercent);
                loanAmount = Integer.valueOf(loanAmountArg);
                futurePayments = parseFuturePayments(simulations);
                System.out.println("Using.. "+args.length+" passed parameters. non passed paramers will use default values");
            } else {
                System.out.println("Ups Unknown param. use: --closeDateStartDateInterestLoanAmountSimulations 2017-12-04 2018-01-01 1000 (2018-01-01=1000, 2018-02-01=100)");
            }


        } else {
            System.out.println("No parameters passed. going with defualt parameters");
        }

        // Render the table
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("Payment", "Month", "Principal","Interest","Remaining Principal");
        at.addRule();

        new ScheduleService(closingDate, startDate, interest, loanAmount, futurePayments).createShedule().forEach(e-> {
            at.addRow(e.getPayment(), e.getPaymentMonth(), e.getPrincipal(),e.getInterest(),e.getRemainingPrincipal());
            at.addRule();
        });

        System.out.println(at.render());

    }

    public static final List<FuturePayment> parseFuturePayments(final String parsableSimulation){
        return Arrays.asList(parsableSimulation
                .substring(parsableSimulation.indexOf("(")+1, parsableSimulation.indexOf(")"))
                .split(","))
                .stream()
                .map(dateAmount-> dateAmount.split("="))
                .map(arr-> new FuturePayment(LocalDate.parse(arr[0].trim(), dtf), new BigDecimal(arr[1].trim())))
                .collect(Collectors.toList());
    }
}
