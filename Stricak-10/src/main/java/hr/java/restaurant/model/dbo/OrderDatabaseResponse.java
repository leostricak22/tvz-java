package hr.java.restaurant.model.dbo;

import java.time.LocalDate;

public class OrderDatabaseResponse {

    private Long id;
    private Long restaurantId;
    private Long delivererId;
    private LocalDate orderDate;

    public OrderDatabaseResponse(Long id, Long restaurantId, Long delivererId, LocalDate orderDate) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.delivererId = delivererId;
        this.orderDate = orderDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Long getDelivererId() {
        return delivererId;
    }

    public void setDelivererId(Long delivererId) {
        this.delivererId = delivererId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
}
