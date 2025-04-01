package models.enums;

public enum ExaminationTypeEnum {
    INITIAL("Първичен преглед"),
    SECONDARY("Вторичен преглед"),
    CONSULTATION("Консултация"),
    PROCEDURE("Медицинска процедура");

    private final String name;

    ExaminationTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
