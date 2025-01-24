package hr.java.restaurant.model.dbo;

import java.math.BigDecimal;

public class IngredientDatabaseResponse {

    private Long id;
    private String name;
    private Long categoryId;
    private BigDecimal kcal;
    private String preparationMethod;

    public IngredientDatabaseResponse(Long id, String name, Long categoryId, BigDecimal kcal, String preparationMethod) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.kcal = kcal;
        this.preparationMethod = preparationMethod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getKcal() {
        return kcal;
    }

    public void setKcal(BigDecimal kcal) {
        this.kcal = kcal;
    }

    public String getPreparationMethod() {
        return preparationMethod;
    }

    public void setPreparationMethod(String preparationMethod) {
        this.preparationMethod = preparationMethod;
    }
}
