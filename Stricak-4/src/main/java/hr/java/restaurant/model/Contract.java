package hr.java.restaurant.model;

import hr.java.service.Input;
import hr.java.service.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a contract of a person.
 */
public class Contract {
    private static final Logger logger = LoggerFactory.getLogger(Contract.class);
    private final  static String[] CONTRACT_TYPES = {"FULL_TIME", "PART_TIME"};
    private final static BigDecimal MIN_SALARY = new BigDecimal(800);

    private final BigDecimal salary;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String contractType;

    /**
     * Constructs a Contract object using the provided builder.
     * @param salary the salary of the contract
     * @param startDate the start date of the contract
     * @param endDate  the end date of the contract
     * @param contractType the type of the contract
     */
    public Contract(BigDecimal salary, LocalDate startDate, LocalDate endDate, String contractType) {
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.contractType = contractType;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public static BigDecimal getMinSalary() {
        return MIN_SALARY;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Prints the contract information.
     * @param tabulators the number of tabulators
     */
    public void print(Integer tabulators) {
        logger.info("Printing contract.");
        Output.tabulatorPrint(tabulators);
        System.out.println("Plaća: " + this.salary + ", Početak ugovora: " + this.startDate + ", Kraj ugovora: " + this.endDate + ", Tip ugovora: " + this.contractType);
    }
}
