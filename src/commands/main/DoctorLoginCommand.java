package commands.main;

import commands.Command;
import controllers.DoctorController;

import java.util.Scanner;

public class DoctorLoginCommand implements Command {
    private final Scanner scanner;

    public DoctorLoginCommand(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        new DoctorController().start(scanner);
    }
}