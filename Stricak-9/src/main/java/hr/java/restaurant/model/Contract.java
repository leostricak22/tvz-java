package hr.java.restaurant.model;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.util.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a contract of a person.
 */
public class Contract extends Entity implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Contract.class);
    private final static BigDecimal MIN_SALARY = new BigDecimal(800);

    private BigDecimal salary;
    private LocalDate startDate;
    private LocalDate endDate;
    private ContractType contractType;
    private boolean active = true;
    private List<String> files = new ArrayList<>();

    /**
     * Constructs a Contract object using the provided builder.
     * @param salary the salary of the contract
     * @param startDate the start date of the contract
     * @param endDate  the end date of the contract
     * @param contractType the type of the contract
     */
    public Contract(Long id, String name, BigDecimal salary, LocalDate startDate, LocalDate endDate, ContractType contractType) {
        super(id, name);
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.contractType = contractType;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        System.out.println("Setting files" + files);
        this.files = files;
    }

    public boolean getActive() {
        return active;
    }

    public String getActiveAsString() {
        return active ? "Aktivan" : "Neaktivan";
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public LocalDate getEndDate() {
        return endDate;
    }

    public ContractType getContractType() {
        return contractType;
    }


}
