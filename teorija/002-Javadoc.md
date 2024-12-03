# Javadoc
služi za pisanje dokumentacije

### Kako pisati Javadoc
```java
/**
 * Represents an ingredient of a meal.
 */
public class Ingredient  extends Entity {
    private static Long counter = 0L;
    private static final Logger logger = LoggerFactory.getLogger(Ingredient.class);

    private String name;
    private final Category category;
    private final BigDecimal kcal;
    private final String preparationMethod;

    /**
     * Constructs an Ingredient object
     * @param name the name of the ingredient
     * @param category the category of the ingredient
     * @param kcal the calories of the ingredient
     * @param preparationMethod the preparation method of the ingredient
     */
    public Ingredient(String name, Category category, BigDecimal kcal, String preparationMethod) {
        super(++counter);
        this.name = name;
        this.category = category;
        this.kcal = kcal;
        this.preparationMethod = preparationMethod;
    }
}
```

### Generiranje Javadoc dokumentacije
- Tools -> Generate Javadoc
- Locale: hr