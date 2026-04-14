package commands.main;

import commands.Command;
import controllers.AdminController;

import java.util.Scanner;

public class AdminLoginCommand implements Command {
    private final Scanner scanner;

    public AdminLoginCommand(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        new AdminController().start(scanner);
    }
}