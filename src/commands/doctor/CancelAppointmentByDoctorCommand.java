package commands.doctor;

import commands.Command;
import controllers.DoctorController;
import models.Doctor;

import java.util.Scanner;

public class CancelAppointmentByDoctorCommand implements Command {
    private final DoctorController doctorController;
    private final Scanner scanner;
    private final Doctor doctor;

    public CancelAppointmentByDoctorCommand(DoctorController doctorController, Scanner scanner, Doctor doctor) {
        this.doctorController = doctorController;
        this.scanner = scanner;
        this.doctor = doctor;
    }

    @Override
    public void execute() {
        doctorController.cancelAppointmentByDoctor(scanner, doctor);
    }
}