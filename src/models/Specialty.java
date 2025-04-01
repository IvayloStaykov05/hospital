package models;

import models.enums.SpecialtyEnum;

public class Specialty extends BaseClass{
    private SpecialtyEnum specialtyEnum;

    public Specialty(int id, SpecialtyEnum specialtyEnum) {
        super(id);
        this.specialtyEnum = specialtyEnum;
    }

    public SpecialtyEnum getSpecialtyEnum() {
        return specialtyEnum;
    }

    public void setSpecialtyEnum(SpecialtyEnum specialtyEnum) {
        this.specialtyEnum = specialtyEnum;
    }

    @Override
    public String toString() {
        return specialtyEnum.getName();
    }
}
