package commands.admin;

import commands.Command;
import controllers.AdminController;

import java.util.Scanner;

public class DeleteDoctorCommand implements Command {
    private final AdminController adminController;
    private final Scanner scanner;

    public DeleteDoctorCommand(AdminController adminController, Scanner scanner) {
        this.adminController = adminController;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        adminController.deleteDoctor(scanner);
    }
}