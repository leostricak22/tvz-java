package hr.java.restaurant.model;

import hr.java.service.Input;
import hr.java.service.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Contract {
    private static final Logger logger = LoggerFactory.getLogger(Contract.class);
    private final  static String[] CONTRACT_TYPES = {"FULL_TIME", "PART_TIME"};
    private final static BigDecimal MIN_SALARY = new BigDecimal(800);

    private BigDecimal salary;
    private LocalDate startDate;
    private LocalDate endDate;
    private String contractType;

    public Contract(BigDecimal salary, LocalDate startDate, LocalDate endDate, String contractType) {
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.contractType = contractType;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public static BigDecimal getMinSalary() {
        return MIN_SALARY;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public static String[] getContractTypes() {
        return CONTRACT_TYPES;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public void print(Integer tabulators) {
        logger.info("Printing contract.");
        Output.tabulatorPrint(tabulators);
        System.out.println("Plaća: " + this.salary + ", Početak ugovora: " + this.startDate + ", Kraj ugovora: " + this.endDate + ", Tip ugovora: " + this.contractType);
    }
}
