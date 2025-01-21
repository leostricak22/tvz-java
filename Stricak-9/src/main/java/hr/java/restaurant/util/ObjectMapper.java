package hr.java.restaurant.util;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.model.*;
import hr.java.restaurant.repository.*;
import hr.java.restaurant.repository.RestaurantChefsRepository;
import hr.java.restaurant.repository.RestaurantMealsRepository;
import hr.java.restaurant.repository.RestaurantDeliverersRepository;
import hr.java.restaurant.repository.RestaurantWaitersRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

public class ObjectMapper {

    private static final AddressRepository addressRepository = new AddressRepository();
    private static final CategoryRepository categoryRepository = new CategoryRepository();
    private static final ContractRepository contractRepository = new ContractRepository();
    private static final RestaurantRepository restaurantRepository = new RestaurantRepository();
    private static final DelivererRepository delivererRepository = new DelivererRepository();

    public static Address mapResultSetToAddress(ResultSet resultSet) throws SQLException {
        return new Address.Builder(resultSet.getLong("ID"))
                .setStreet(resultSet.getString("STREET"))
                .setHouseNumber(resultSet.getString("HOUSE_NUMBER"))
                .setCity(resultSet.getString("CITY"))
                .setPostalCode(resultSet.getString("POSTAL_CODE"))
                .build();
    }

    public static Category mapResultSetToCategory(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("ID");
        String name = resultSet.getString("NAME");
        String description = resultSet.getString("DESCRIPTION");

        return new Category.Builder(id, name)
                .setDescription(description)
                .build();
    }

    public static Chef mapResultSetToChef(ResultSet resultSet) throws SQLException {
        return new Chef.Builder(resultSet.getLong("ID"))
                .setFirstName(resultSet.getString("FIRST_NAME"))
                .setLastName(resultSet.getString("LAST_NAME"))
                .setContract(contractRepository.findById(resultSet.getLong("CONTRACT_ID")))
                .setBonus(new Bonus(resultSet.getBigDecimal("BONUS")))
                .build();
    }

    public static Contract mapResultSetToContract(ResultSet resultSet) throws SQLException {
        return new Contract(
                resultSet.getLong("ID"),
                "Contract-"+resultSet.getLong("ID"),
                resultSet.getBigDecimal("SALARY"),
                resultSet.getDate("START_DATE").toLocalDate(),
                resultSet.getDate("END_DATE") == null ?
                        null :
                        resultSet.getDate("END_DATE").toLocalDate(),
                ContractType.valueOf(resultSet.getString("TYPE"))
        );
    }

    public static Deliverer mapResultSetToDeliverer(ResultSet resultSet) throws SQLException {
        return new Deliverer.Builder(resultSet.getLong("ID"))
                .setFirstName(resultSet.getString("FIRST_NAME"))
                .setLastName(resultSet.getString("LAST_NAME"))
                .setContract(contractRepository.findById(resultSet.getLong("CONTRACT_ID")))
                .setBonus(new Bonus(resultSet.getBigDecimal("BONUS")))
                .build();
    }

    public static Ingredient mapResultSetToIngredient(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("ID");
        String name = resultSet.getString("NAME");
        Category category = categoryRepository.findById(resultSet.getLong("CATEGORY_ID"));
        BigDecimal kcal = resultSet.getBigDecimal("KCAL");
        String preparationMethod = resultSet.getString("PREPARATION_METHOD");

        return new Ingredient.Builder(id, name)
                .setCategory(category)
                .setKcal(kcal)
                .setPreparationMethod(preparationMethod)
                .build();
    }

    public static Meal mapResultSetToMeal(ResultSet resultSet) throws SQLException, IOException {
        Long id = resultSet.getLong("ID");
        String name = resultSet.getString("NAME");
        Category category = categoryRepository.findById(resultSet.getLong("CATEGORY_ID"));
        BigDecimal price = resultSet.getBigDecimal("PRICE");
        Set<Ingredient> ingredients = MealIngredientsRepository.getIngredientsForMeal(id);

        return new Meal.Builder(id, name)
                .setCategory(category)
                .setPrice(price)
                .setIngredients(ingredients)
                .build();
    }

    public static Order mapResultSetToOrder(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("ID");
        Restaurant restaurant = restaurantRepository.findById(resultSet.getLong("RESTAURANT_ID"));
        Set<Meal> meals = OrderMealsRepository.getMealsForOrder(id);
        Deliverer deliverer = delivererRepository.findById(resultSet.getLong("DELIVERER_ID"));
        LocalDateTime deliveryDateAndTime = resultSet.getTimestamp("DATE_AND_TIME").toLocalDateTime();

        return new Order(id, restaurant, new ArrayList<>(meals), deliverer, deliveryDateAndTime);
    }

    public static Restaurant mapResultSetToRestaurant(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("ID");
        String name = resultSet.getString("NAME");
        Address address = addressRepository.findById(resultSet.getLong("ADDRESS_ID"));

        Set<Meal> meals = RestaurantMealsRepository.getMealsForRestaurant(id);
        Set<Chef> chefs = RestaurantChefsRepository.getChefsForRestaurant(id);
        Set<Waiter> waiters = RestaurantWaitersRepository.getWaitersForRestaurant(id);
        Set<Deliverer> deliverers = RestaurantDeliverersRepository.getDeliverersForRestaurant(id);

        return new Restaurant(id, name, address, meals, chefs, waiters, deliverers);
    }

    public static Waiter mapResultSetToWaiter(ResultSet resultSet) throws SQLException {
        return new Waiter.Builder(resultSet.getLong("ID"))
                .setFirstName(resultSet.getString("FIRST_NAME"))
                .setLastName(resultSet.getString("LAST_NAME"))
                .setContract(contractRepository.findById(resultSet.getLong("CONTRACT_ID")))
                .setBonus(new Bonus(resultSet.getBigDecimal("BONUS")))
                .build();
    }
}
