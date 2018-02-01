** Running **
Just an example of a morgage shedule using java 8

* Compiling *

`mvn clean install` This should generate a jar with all dependencies in it

* Running *

`java -jar test-1.0-SNAPSHOT-jar-with-dependencies.jar --closeDateStartDateInterestLoanAmountSimulations 2017-12-01 2018-01-01 12 100000 '[2018-02-01=1000, 2018-04-02=3000]'
`

you need to pass `--closeDateStartDateInterestLoanAmountSimulations closeDate startDate percentAmount loanAmount [paymentSimulations]`

using the following sintax
`closeDate`: yyyy-MM-dd ; Closing Date
`startDate`: yyyy-MM-dd ; Date user starts paying
`percentAmount`: Int ; Integer in percentage
`loanAmount`: Int ; Integer representing loan amount
`[paymentSimulations]`: String ; inside the `[]` You must provide simulated date `=` amount you want to simulate



* Known Issues or things not taken into account *

> In payment simualtions im not validating repeated dates. nor paying during the same month, im expecting dates within the term.

> Period as spoken remains constant

> If a parameter is not correcltly detected it will use the default values

> Has a Decimal approximation issue so it wont get to exactly Zero.
