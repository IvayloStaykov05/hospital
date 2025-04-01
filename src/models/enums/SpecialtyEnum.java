package models.enums;

public enum SpecialtyEnum {
    ANESTHESIOLOGY("Анестезиология"),
    INTERNAL_DISEASES("Вътрешни болести"),
    GASTROENTEROLOGY("Гастроентерология"),
    ENDOCRINOLOGY("Ендокринология"),
    CARDIOLOGY("Кардиология"),
    DERMATOLOGY("Дерматология"),
    NEUROLOGY("Неврология"),
    NEPHROLOGY("Нефрология"),
    PSYCHIATRY("Психиатрия"),
    RHEUMATOLOGY("Ревматология"),
    GYNECOLOGY("Гинекология"),
    ORTHOPEDICS("Ортопедия"),
    OPHTHALMOLOGY("Очни болести"),
    UROLOGY("Урология"),
    SURGERY("Хирургия");

    private final String name;

    SpecialtyEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
