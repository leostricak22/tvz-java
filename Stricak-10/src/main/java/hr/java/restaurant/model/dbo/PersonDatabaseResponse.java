package hr.java.restaurant.model.dbo;

import java.math.BigDecimal;

public class PersonDatabaseResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private BigDecimal bonus;
    private Long contractId;

    public PersonDatabaseResponse(Long id, String firstName, String lastName, BigDecimal bonus, Long contractId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bonus = bonus;
        this.contractId = contractId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }
}
