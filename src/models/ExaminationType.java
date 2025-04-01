package models;

import models.enums.ExaminationTypeEnum;

public class ExaminationType extends BaseClass{
    private ExaminationTypeEnum examinationTypeEnum;

    public ExaminationType(int id, ExaminationTypeEnum examinationTypeEnum) {
        super(id);
        this.examinationTypeEnum = examinationTypeEnum;
    }

    public ExaminationTypeEnum getExaminationTypeEnum() {
        return examinationTypeEnum;
    }

    public void setExaminationTypeEnum(ExaminationTypeEnum examinationTypeEnum) {
        this.examinationTypeEnum = examinationTypeEnum;
    }

    @Override
    public String toString() {
        return examinationTypeEnum.getName();
    }
}
