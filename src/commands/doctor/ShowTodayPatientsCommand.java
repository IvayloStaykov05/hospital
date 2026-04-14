package commands.doctor;

import commands.Command;
import controllers.DoctorController;
import models.Doctor;

public class ShowTodayPatientsCommand implements Command {
    private final DoctorController doctorController;
    private final Doctor doctor;

    public ShowTodayPatientsCommand(DoctorController doctorController, Doctor doctor) {
        this.doctorController = doctorController;
        this.doctor = doctor;
    }

    @Override
    public void execute() {
        doctorController.showTodayPatients(doctor);
    }
}