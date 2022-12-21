package by.bstu.vs.stpms.courier_application.model.network.dto;

import java.sql.Timestamp;
import java.util.Collection;

public class OrderDto extends AbstractDto {
    private String info;
    private String state;
    private UserDto customer;
    private UserDto courier;
    private Collection<OrderedDto> ordered;

    private Timestamp orderedAt;
    private Timestamp takenAt;
    private Timestamp deliveredAt;

    private DestinationDto sender;
    private DestinationDto recipient;


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public UserDto getCustomer() {
        return customer;
    }

    public void setCustomer(UserDto customer) {
        this.customer = customer;
    }

    public UserDto getCourier() {
        return courier;
    }

    public void setCourier(UserDto courier) {
        this.courier = courier;
    }

    public Collection<OrderedDto> getOrdered() {
        return ordered;
    }

    public void setOrdered(Collection<OrderedDto> ordered) {
        this.ordered = ordered;
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

    public DestinationDto getSender() {
        return sender;
    }

    public void setSender(DestinationDto sender) {
        this.sender = sender;
    }

    public DestinationDto getRecipient() {
        return recipient;
    }

    public void setRecipient(DestinationDto recipient) {
        this.recipient = recipient;
    }
}
