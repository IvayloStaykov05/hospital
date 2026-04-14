package commands.patient;

import commands.Command;
import controllers.PatientController;

import java.util.Scanner;

public class UpdateAppointmentCommand implements Command {
    private final PatientController patientController;
    private final Scanner scanner;

    public UpdateAppointmentCommand(PatientController patientController, Scanner scanner) {
        this.patientController = patientController;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        patientController.updateAppointment(scanner);
    }
}