package hr.java.restaurant.enumeration;

import static java.util.Optional.ofNullable;

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

    public static ContractType valueOfByName(String name) {
        return ofNullable(name)
                .map(n -> switch (name) {
                    case "Full time" -> FULL_TIME;
                    case "Part time" -> PART_TIME;
                    default -> null;
                }).orElseThrow(() -> new IllegalArgumentException("Unknown contract type: " + name));
    }
}
