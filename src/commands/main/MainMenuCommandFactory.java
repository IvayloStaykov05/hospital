package commands.main;

import commands.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MainMenuCommandFactory {
    private final Map<String, Command> commands = new HashMap<>();

    public MainMenuCommandFactory(Scanner scanner) {
        commands.put("1", new PatientLoginCommand(scanner));
        commands.put("2", new DoctorLoginCommand(scanner));
        commands.put("3", new AdminLoginCommand(scanner));
        commands.put("0", new ExitCommand());
    }

    public Command getCommand(String input) {
        return commands.get(input);
    }
}