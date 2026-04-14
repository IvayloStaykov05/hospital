package commands.admin;

import commands.Command;
import controllers.AdminController;

public class ShowAllPatientsCommand implements Command {
    private final AdminController adminController;

    public ShowAllPatientsCommand(AdminController adminController) {
        this.adminController = adminController;
    }

    @Override
    public void execute() {
        adminController.showAllPatients();
    }
}