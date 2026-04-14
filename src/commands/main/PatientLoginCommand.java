package commands.main;

import commands.Command;
import controllers.PatientController;

import java.util.Scanner;

public class PatientLoginCommand implements Command {
    private final Scanner scanner;

    public PatientLoginCommand(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        new PatientController().start(scanner);
    }
}