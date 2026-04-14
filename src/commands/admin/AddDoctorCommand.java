package commands.admin;

import commands.Command;
import controllers.AdminController;

import java.util.Scanner;

public class AddDoctorCommand implements Command {
    private final AdminController adminController;
    private final Scanner scanner;

    public AddDoctorCommand(AdminController adminController, Scanner scanner) {
        this.adminController = adminController;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        adminController.addDoctor(scanner);
    }
}