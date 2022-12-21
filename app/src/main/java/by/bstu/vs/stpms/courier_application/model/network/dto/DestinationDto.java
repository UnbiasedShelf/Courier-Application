package by.bstu.vs.stpms.courier_application.model.network.dto;

import java.sql.Timestamp;
import java.util.Objects;

public class DestinationDto extends AbstractDto {
    private String name;
    private String phone;
    private String address;
    private String rangeStart;
    private String rangeEnd;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(String rangeStart) {
        this.rangeStart = rangeStart;
    }

    public String getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(String rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DestinationDto that = (DestinationDto) o;
        return name.equals(that.name) && phone.equals(that.phone) && address.equals(that.address) && Objects.equals(rangeStart, that.rangeStart) && Objects.equals(rangeEnd, that.rangeEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, phone, address, rangeStart, rangeEnd);
    }

    @Override
    public String toString() {
        return "DestinationDto{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", rangeStart=" + rangeStart +
                ", rangeEnd=" + rangeEnd +
                '}';
    }
}
