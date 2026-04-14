package commands.doctor;

import commands.Command;
import controllers.DoctorController;
import models.Doctor;
import models.enums.StatusEnum;

public class ShowCanceledAppointmentsCommand implements Command {
    private final DoctorController doctorController;
    private final Doctor doctor;

    public ShowCanceledAppointmentsCommand(DoctorController doctorController, Doctor doctor) {
        this.doctorController = doctorController;
        this.doctor = doctor;
    }

    @Override
    public void execute() {
        doctorController.showAppointmentsByStatus(doctor, StatusEnum.CANCELED);
    }
}