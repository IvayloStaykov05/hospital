package commands.admin;

import commands.Command;
import controllers.AdminController;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdminMenuCommandFactory {
    private final Map<String, Command> commands = new HashMap<>();

    public AdminMenuCommandFactory(AdminController adminController, Scanner scanner) {
        commands.put("1", new ShowAllDoctorsCommand(adminController));
        commands.put("2", new AddDoctorCommand(adminController, scanner));
        commands.put("3", new DeleteDoctorCommand(adminController, scanner));
        commands.put("4", new ShowAllPatientsCommand(adminController));
        commands.put("5", new EditDoctorCommand(adminController, scanner));
        commands.put("6", new AddPatientByAdminCommand(adminController, scanner));
    }

    public Command getCommand(String input) {
        return commands.get(input);
    }
}