package commands.patient;

import commands.Command;
import controllers.PatientController;
import models.Patient;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PatientMenuCommandFactory {
    private final Map<String, Command> commands = new HashMap<>();

    public PatientMenuCommandFactory(PatientController patientController, Scanner scanner, Patient patient) {
        commands.put("1", new ShowAppointmentsCommand(patientController, patient));
        commands.put("2", new BookAppointmentCommand(patientController, scanner, patient));
        commands.put("3", new UpdateAppointmentCommand(patientController, scanner));
        commands.put("4", new CancelAppointmentCommand(patientController, scanner));
    }

    public Command getCommand(String input) {
        return commands.get(input);
    }
}