package hr.java.restaurant.enumeration;

public enum ContractType {
    FULL_TIME("Full time"),
    PART_TIME("Part time");

    private final String name;

    ContractType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
