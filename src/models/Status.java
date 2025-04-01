package models;

import models.enums.StatusEnum;

public class Status extends BaseClass{
    private StatusEnum statusEnum;

    public Status(int id, StatusEnum statusEnum) {
        super(id);
        this.statusEnum = statusEnum;
    }

    public StatusEnum getStatusEnum() {
        return statusEnum;
    }

    public void setStatusEnum(StatusEnum statusEnum) {
        this.statusEnum = statusEnum;
    }

    @Override
    public String toString() {
        return statusEnum.getName();
    }
}
