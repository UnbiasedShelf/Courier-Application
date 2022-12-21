package by.bstu.vs.stpms.courier_application.model.database.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Set;

import by.bstu.vs.stpms.courier_application.model.database.entity.converters.CourierTypeConverter;
import by.bstu.vs.stpms.courier_application.model.database.entity.converters.OrderStateConverter;
import by.bstu.vs.stpms.courier_application.model.database.entity.converters.TimestampConverter;
import by.bstu.vs.stpms.courier_application.model.database.entity.enums.CourierType;

@Entity(tableName = "user")
@TypeConverters({CourierTypeConverter.class})
public class User extends AbstractEntity implements Serializable {
    private String firstName;
    private String secondName;
    private String email;
    private String phone;
    private String password;
    private CourierType courierType;

    @Ignore
    private Set<Role> roles;

    public User() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CourierType getCourierType() {
        return courierType;
    }

    public void setCourierType(CourierType courierType) {
        this.courierType = courierType;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
