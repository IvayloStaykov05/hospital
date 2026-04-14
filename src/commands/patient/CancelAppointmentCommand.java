package commands.patient;

import commands.Command;
import controllers.PatientController;

import java.util.Scanner;

public class CancelAppointmentCommand implements Command {
    private final PatientController patientController;
    private final Scanner scanner;

    public CancelAppointmentCommand(PatientController patientController, Scanner scanner) {
        this.patientController = patientController;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        patientController.cancelAppointment(scanner);
    }
}