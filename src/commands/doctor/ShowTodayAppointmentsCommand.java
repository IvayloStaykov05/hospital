package commands.doctor;

import commands.Command;
import controllers.DoctorController;
import models.Doctor;

public class ShowTodayAppointmentsCommand implements Command {
    private final DoctorController doctorController;
    private final Doctor doctor;

    public ShowTodayAppointmentsCommand(DoctorController doctorController, Doctor doctor) {
        this.doctorController = doctorController;
        this.doctor = doctor;
    }

    @Override
    public void execute() {
        doctorController.showAppointmentsToday(doctor);
    }
}