package com.whiteprompt;

import com.whiteprompt.model.FuturePayment;
import de.vandermeer.asciitable.AsciiTable;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        LocalDate startDate = LocalDate.of(2018, Month.JANUARY,1);
        LocalDate closingDate = LocalDate.of(2017, Month.DECEMBER, 15);
        int interest = 12;
        int loanAmount = 100000;

        List<FuturePayment> futurePaymentList = Arrays
                .asList(new FuturePayment(startDate.plusMonths(3), new BigDecimal(123)));

        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("Payment", "Month", "Principal","Interest","Remaining Principal");
        at.addRule();

        new ScheduleService(closingDate, startDate, interest, loanAmount, futurePaymentList)
                .createShedule()
                .forEach(e-> {
            at.addRow(e.getPayment(), e.getPaymentMonth(), e.getPrincipal(),e.getInterest(),e.getRemainingPrincipal());
            at.addRule();
        });

        System.out.println(at.render());

    }
}
