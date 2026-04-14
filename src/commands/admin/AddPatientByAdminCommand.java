package commands.admin;

import commands.Command;
import controllers.AdminController;

import java.util.Scanner;

public class AddPatientByAdminCommand implements Command {
    private final AdminController adminController;
    private final Scanner scanner;

    public AddPatientByAdminCommand(AdminController adminController, Scanner scanner) {
        this.adminController = adminController;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        adminController.addPatients(scanner);
    }
}