package com.whiteprompt;

import de.vandermeer.asciitable.AsciiTable;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        LocalDate startDate = LocalDate.of(2018, Month.JANUARY,1);
        LocalDate closingDate = LocalDate.of(2017, Month.DECEMBER, 15);
        int interest = 12;

        if(args.length > 1 ){
            if(args[0].equals("--closeDateStartDateInterest")) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String closeDateArg = args[1];
                String startDateArg = args[2];
                String interestPercent = args[3];

                startDate = LocalDate.parse(startDateArg, dtf);
                closingDate = LocalDate.parse(closeDateArg, dtf);
                interest = Integer.valueOf(interestPercent);
                System.out.println("Using passed parameters...");
            } else {
                System.out.println("Ups Unknown param. use: --closeStartDate 2017-12-04 2018-01-01");
            }


        }

        // Render the table
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("Payment", "Month", "Principal","Interest","Remaining Principal");
        at.addRule();

        ScheduleService.createShedule(closingDate, startDate, interest).forEach(e-> {
            at.addRow(e.getPayment(), e.getPaymentMonth(), e.getPrincipal(),e.getInterest(),e.getRemainingPrincipal());
            at.addRule();
        });

        System.out.println(at.render());

    }
}
