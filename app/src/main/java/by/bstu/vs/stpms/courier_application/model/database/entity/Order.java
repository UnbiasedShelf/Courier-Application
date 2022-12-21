package by.bstu.vs.stpms.courier_application.model.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

import by.bstu.vs.stpms.courier_application.model.database.entity.converters.OrderStateConverter;
import by.bstu.vs.stpms.courier_application.model.database.entity.converters.TimestampConverter;
import by.bstu.vs.stpms.courier_application.model.database.entity.enums.OrderState;

@Entity(
        tableName = "orders",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "customerId"),
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "courierId"),
                @ForeignKey(entity = Destination.class, parentColumns = "id", childColumns = "senderId"),
                @ForeignKey(entity = Destination.class, parentColumns = "id", childColumns = "recipientId"),
        }
)
@TypeConverters({TimestampConverter.class, OrderStateConverter.class})
public class Order extends AbstractEntity implements Serializable {
    private String info;
    private OrderState state;
    private Long customerId;
    private Long courierId;

    private Timestamp orderedAt;
    private Timestamp takenAt;
    private Timestamp deliveredAt;

    private long senderId;
    private long recipientId;

    @Ignore
    private Collection<Ordered> ordered;
    @Ignore
    private User customer;
    @Ignore
    private User courier;
    @Ignore
    private Destination sender;
    @Ignore
    private Destination recipient;

    public double getTotalPrice() {
        double price = 0.0;
        if (ordered != null) {
            for (Ordered productAmount: ordered) {
                Product product = productAmount.getProduct();
                if (product != null) {
                    price += productAmount.getAmount() * product.getPrice();
                }
            }
        }
        return price;
    }

    public double getTotalWeight() {
        double weight = 0.0;
        if (ordered != null) {
            for (Ordered productAmount: ordered) {
                Product product = productAmount.getProduct();
                if (product != null) {
                    weight += productAmount.getAmount() * product.getWeight();
                }
            }
        }
        return weight;
    }

    public double calculateCost() {
        double cost;
        double weight = getTotalWeight();

        if (weight < 9.0) {
            cost = 5.0;
        } else if (weight >= 9.0 && weight < 25.0) {
            cost = 10.0;
        } else {
            cost = 25.0;
        }

        return cost;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getCourierId() {
        return courierId;
    }

    public void setCourierId(Long courierId) {
        this.courierId = courierId;
    }

    public Timestamp getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(Timestamp orderedAt) {
        this.orderedAt = orderedAt;
    }

    public Timestamp getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(Timestamp takenAt) {
        this.takenAt = takenAt;
    }

    public Timestamp getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(Timestamp deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(long recipientId) {
        this.recipientId = recipientId;
    }

    public Collection<Ordered> getOrdered() {
        return ordered;
    }

    public void setOrdered(Collection<Ordered> ordered) {
        this.ordered = ordered;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public User getCourier() {
        return courier;
    }

    public void setCourier(User courier) {
        this.courier = courier;
    }

    public Destination getSender() {
        return sender;
    }

    public void setSender(Destination sender) {
        this.sender = sender;
    }

    public Destination getRecipient() {
        return recipient;
    }

    public void setRecipient(Destination recipient) {
        this.recipient = recipient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return senderId == order.senderId && recipientId == order.recipientId && Objects.equals(info, order.info) && state == order.state && Objects.equals(customerId, order.customerId) && Objects.equals(courierId, order.courierId) && Objects.equals(orderedAt, order.orderedAt) && Objects.equals(takenAt, order.takenAt) && Objects.equals(deliveredAt, order.deliveredAt) && Objects.equals(ordered, order.ordered) && customer.equals(order.customer) && Objects.equals(courier, order.courier) && Objects.equals(sender, order.sender) && Objects.equals(recipient, order.recipient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(info, state, customerId, courierId, orderedAt, takenAt, deliveredAt, senderId, recipientId, ordered, customer, courier, sender, recipient);
    }

    @Override
    public String toString() {
        return "Order{" +
                "info='" + info + '\'' +
                ", state=" + state +
                ", customerId=" + customerId +
                ", courierId=" + courierId +
                ", orderedAt=" + orderedAt +
                ", takenAt=" + takenAt +
                ", deliveredAt=" + deliveredAt +
                ", senderId=" + senderId +
                ", recipientId=" + recipientId +
                ", ordered=" + ordered +
                ", customer=" + customer +
                ", courier=" + courier +
                ", sender=" + sender +
                ", recipient=" + recipient +
                '}';
    }
}
