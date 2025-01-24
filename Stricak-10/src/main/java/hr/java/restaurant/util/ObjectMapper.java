package hr.java.restaurant.util;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.model.*;
import hr.java.restaurant.model.dbo.*;
import hr.java.restaurant.repository.*;
import hr.java.restaurant.repository.RestaurantChefsRepository;
import hr.java.restaurant.repository.RestaurantMealsRepository;
import hr.java.restaurant.repository.RestaurantDeliverersRepository;
import hr.java.restaurant.repository.RestaurantWaitersRepository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
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

    public static Ingredient mapIngredientDatabaseResponseToIngredient(IngredientDatabaseResponse ingredientDatabaseResponse) {
        return new Ingredient.Builder(ingredientDatabaseResponse.getId(), ingredientDatabaseResponse.getName())
                .setCategory(categoryRepository.findById(ingredientDatabaseResponse.getCategoryId()))
                .setKcal(ingredientDatabaseResponse.getKcal())
                .setPreparationMethod(ingredientDatabaseResponse.getPreparationMethod())
                .build();
    }

    public static Set<Ingredient> mapIngredientDatabaseResponsesToIngredients(Set<IngredientDatabaseResponse> ingredientDatabaseResponses) {
        Set<Ingredient> ingredients = new HashSet<>();

        for (IngredientDatabaseResponse ingredientDatabaseResponse : ingredientDatabaseResponses) {
            ingredients.add(mapIngredientDatabaseResponseToIngredient(ingredientDatabaseResponse));
        }

        return ingredients;
    }

    public static IngredientDatabaseResponse mapResultSetToIngredientDatabaseResponse(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("ID");
        String name = resultSet.getString("NAME");
        Long categoryId = resultSet.getLong("CATEGORY_ID");
        BigDecimal kcal = resultSet.getBigDecimal("KCAL");
        String preparationMethod = resultSet.getString("PREPARATION_METHOD");

        return new IngredientDatabaseResponse(id, name, categoryId, kcal, preparationMethod);
    }

    public static Meal mapMealDatabaseResponseToMeal(MealDatabaseResponse mealDatabaseResponse) {
        return new Meal.Builder(mealDatabaseResponse.getId(), mealDatabaseResponse.getName())
                .setCategory(categoryRepository.findById(mealDatabaseResponse.getCategoryId()))
                .setPrice(mealDatabaseResponse.getPrice())
                .setIngredients(MealIngredientsRepository.getIngredientsForMeal(mealDatabaseResponse.getId()))
                .build();
    }

    public static Set<Meal> mapMealDatabaseResponsesToMeals(Set<MealDatabaseResponse> mealDatabaseResponses) {
        Set<Meal> meals = new HashSet<>();

        for (MealDatabaseResponse mealDatabaseResponse : mealDatabaseResponses) {
            meals.add(mapMealDatabaseResponseToMeal(mealDatabaseResponse));
        }

        return meals;
    }

    public static MealDatabaseResponse mapResultSetToMealDatabaseResponse(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("ID");
        String name = resultSet.getString("NAME");
        Long categoryId = resultSet.getLong("CATEGORY_ID");
        BigDecimal price = resultSet.getBigDecimal("PRICE");

        return new MealDatabaseResponse(id, name, categoryId, price);
    }

    public static Chef mapChefDatabaseResponseToChef(PersonDatabaseResponse chefDatabaseResponse) {
        return new Chef.Builder(chefDatabaseResponse.getId())
                .setFirstName(chefDatabaseResponse.getFirstName())
                .setLastName(chefDatabaseResponse.getLastName())
                .setContract(contractRepository.findById(chefDatabaseResponse.getContractId()))
                .setBonus(new Bonus(chefDatabaseResponse.getBonus()))
                .build();
    }

    public static Set<Chef> mapChefDatabaseResponsesToChefs(Set<PersonDatabaseResponse> chefDatabaseResponses) {
        Set<Chef> chefs = new HashSet<>();

        for (PersonDatabaseResponse chefDatabaseResponse : chefDatabaseResponses) {
            chefs.add(mapChefDatabaseResponseToChef(chefDatabaseResponse));
        }

        return chefs;
    }

    public static PersonDatabaseResponse mapResultSetToPersonDatabaseResponse(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("ID");
        String firstName = resultSet.getString("FIRST_NAME");
        String lastName = resultSet.getString("LAST_NAME");
        Long contractId = resultSet.getLong("CONTRACT_ID");
        BigDecimal bonus = resultSet.getBigDecimal("BONUS");

        return new PersonDatabaseResponse(id, firstName, lastName, bonus, contractId);
    }

    public static Waiter mapWaiterDatabaseResponseToWaiter(PersonDatabaseResponse chefDatabaseResponse) {
        return new Waiter.Builder(chefDatabaseResponse.getId())
                .setFirstName(chefDatabaseResponse.getFirstName())
                .setLastName(chefDatabaseResponse.getLastName())
                .setContract(contractRepository.findById(chefDatabaseResponse.getContractId()))
                .setBonus(new Bonus(chefDatabaseResponse.getBonus()))
                .build();
    }

    public static Set<Waiter> mapWaiterDatabaseResponsesToWaiters(Set<PersonDatabaseResponse> waiterDatabaseResponses) {
        Set<Waiter> waiters = new HashSet<>();

        for (PersonDatabaseResponse waiterDatabaseResponse : waiterDatabaseResponses) {
            waiters.add(mapWaiterDatabaseResponseToWaiter(waiterDatabaseResponse));
        }

        return waiters;
    }

    public static Deliverer mapDelivererDatabaseResponseToDeliverer(PersonDatabaseResponse delivererDatabaseResponse) {
        return new Deliverer.Builder(delivererDatabaseResponse.getId())
                .setFirstName(delivererDatabaseResponse.getFirstName())
                .setLastName(delivererDatabaseResponse.getLastName())
                .setContract(contractRepository.findById(delivererDatabaseResponse.getContractId()))
                .setBonus(new Bonus(delivererDatabaseResponse.getBonus()))
                .build();
    }

    public static Set<Deliverer> mapDelivererDatabaseResponsesToDeliverers(Set<PersonDatabaseResponse> delivererDatabaseResponses) {
        Set<Deliverer> deliverers = new HashSet<>();

        for (PersonDatabaseResponse delivererDatabaseResponse : delivererDatabaseResponses) {
            deliverers.add(mapDelivererDatabaseResponseToDeliverer(delivererDatabaseResponse));
        }

        return deliverers;
    }

    public static RestaurantDatabaseResponse mapResultSetToRestaurantDatabaseResponse(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("ID");
        String name = resultSet.getString("NAME");
        Long addressId = resultSet.getLong("ADDRESS_ID");

        return new RestaurantDatabaseResponse(id, name, addressId);
    }

    public static Restaurant mapRestaurantDatabaseResponseToRestaurant(RestaurantDatabaseResponse restaurantDatabaseResponse) {
        return new Restaurant(restaurantDatabaseResponse.getId(), restaurantDatabaseResponse.getName(),
                addressRepository.findById(restaurantDatabaseResponse.getAddressId()),
                RestaurantMealsRepository.getMealsForRestaurant(restaurantDatabaseResponse.getId()),
                RestaurantChefsRepository.getChefsForRestaurant(restaurantDatabaseResponse.getId()),
                RestaurantWaitersRepository.getWaitersForRestaurant(restaurantDatabaseResponse.getId()),
                RestaurantDeliverersRepository.getDeliverersForRestaurant(restaurantDatabaseResponse.getId()));
    }

    public static Set<Restaurant> mapRestaurantDatabaseResponsesToRestaurants(Set<RestaurantDatabaseResponse> restaurantDatabaseResponses) {
        Set<Restaurant> restaurants = new HashSet<>();

        for (RestaurantDatabaseResponse restaurantDatabaseResponse : restaurantDatabaseResponses) {
            restaurants.add(mapRestaurantDatabaseResponseToRestaurant(restaurantDatabaseResponse));
        }

        return restaurants;
    }

    public static Order mapOrderDatabaseResponseToOrder(OrderDatabaseResponse orderDatabaseResponse) {
        return new Order(orderDatabaseResponse.getId(),
                restaurantRepository.findById(orderDatabaseResponse.getRestaurantId()),
                new ArrayList<>(OrderMealsRepository.getMealsForOrder(orderDatabaseResponse.getId())),
                delivererRepository.findById(orderDatabaseResponse.getDelivererId()),
                orderDatabaseResponse.getOrderDate().atStartOfDay());
    }

    public static Set<Order> mapOrderDatabaseResponsesToOrders(Set<OrderDatabaseResponse> orderDatabaseResponses) {
        Set<Order> orders = new HashSet<>();

        for (OrderDatabaseResponse orderDatabaseResponse : orderDatabaseResponses) {
            orders.add(mapOrderDatabaseResponseToOrder(orderDatabaseResponse));
        }

        return orders;
    }

    public static OrderDatabaseResponse mapResultSetToOrderDatabaseResponse(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("ID");
        Long restaurantId = resultSet.getLong("RESTAURANT_ID");
        Long delivererId = resultSet.getLong("DELIVERER_ID");
        LocalDate deliveryDateAndTime = resultSet.getTimestamp("DATE_AND_TIME").toLocalDateTime().toLocalDate();

        return new OrderDatabaseResponse(id, restaurantId, delivererId, deliveryDateAndTime);
    }

    public static Order mapResultSetToOrder(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("ID");
        Restaurant restaurant = restaurantRepository.findById(resultSet.getLong("RESTAURANT_ID"));
        Set<Meal> meals = OrderMealsRepository.getMealsForOrder(id);
        Deliverer deliverer = delivererRepository.findById(resultSet.getLong("DELIVERER_ID"));
        LocalDateTime deliveryDateAndTime = resultSet.getTimestamp("DATE_AND_TIME").toLocalDateTime();

        return new Order(id, restaurant, new ArrayList<>(meals), deliverer, deliveryDateAndTime);
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
