package commands.doctor;

import commands.Command;
import controllers.DoctorController;
import models.Doctor;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DoctorMenuCommandFactory {
    private final Map<String, Command> commands = new HashMap<>();

    public DoctorMenuCommandFactory(DoctorController doctorController, Scanner scanner, Doctor doctor) {
        commands.put("1", new ShowUpcomingAppointmentsCommand(doctorController, doctor));
        commands.put("2", new ShowPastAppointmentsCommand(doctorController, doctor));
        commands.put("3", new ShowCanceledAppointmentsCommand(doctorController, doctor));
        commands.put("4", new ShowTodayAppointmentsCommand(doctorController, doctor));
        commands.put("5", new ShowTodayPatientsCommand(doctorController, doctor));
        commands.put("6", new CancelAppointmentByDoctorCommand(doctorController, scanner, doctor));
    }

    public Command getCommand(String input) {
        return commands.get(input);
    }
}