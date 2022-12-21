package by.bstu.vs.stpms.courier_application.model.database.entity;

import androidx.room.Entity;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import by.bstu.vs.stpms.courier_application.model.database.entity.converters.TimestampConverter;

@Entity(tableName = "destination")
@TypeConverters({TimestampConverter.class})
public class Destination extends AbstractEntity implements Serializable {
    private String name = "";
    private String phone = "";
    private String address = "";
    private Timestamp rangeStart;
    private Timestamp rangeEnd;

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

    public Timestamp getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(Timestamp rangeStart) {
        this.rangeStart = rangeStart;
    }

    public Timestamp getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(Timestamp rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Destination that = (Destination) o;
        return name.equals(that.name) && phone.equals(that.phone) && address.equals(that.address) && Objects.equals(rangeStart, that.rangeStart) && Objects.equals(rangeEnd, that.rangeEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, phone, address, rangeStart, rangeEnd);
    }

    @Override
    public String toString() {
        return "Destination{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", rangeStart=" + rangeStart +
                ", rangeEnd=" + rangeEnd +
                '}';
    }
}
