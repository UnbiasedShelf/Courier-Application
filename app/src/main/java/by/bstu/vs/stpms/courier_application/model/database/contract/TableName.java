package by.bstu.vs.stpms.courier_application.model.database.contract;

public enum TableName {
    ORDER("orders"),
    ORDERED("ordered"),
    PRODUCT("product"),
    ROLE("role"),
    USER("user");

    private final String name;

    TableName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
