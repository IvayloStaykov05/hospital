package models.enums;

public enum StatusEnum {
    PAST("Отминал"),
    UPCOMING("Предстоящ"),
    CANCELED("Отменен");

    private final String name;

    StatusEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
