package commands.patient;

import commands.Command;
import controllers.PatientController;
import models.Patient;

import java.util.Scanner;

public class BookAppointmentCommand implements Command {
    private final PatientController patientController;
    private final Scanner scanner;
    private final Patient patient;

    public BookAppointmentCommand(PatientController patientController, Scanner scanner, Patient patient) {
        this.patientController = patientController;
        this.scanner = scanner;
        this.patient = patient;
    }

    @Override
    public void execute() {
        patientController.bookAppointment(scanner, patient);
    }
}