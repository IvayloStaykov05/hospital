package commands.admin;

import commands.Command;
import controllers.AdminController;

public class ShowAllDoctorsCommand implements Command {
    private final AdminController adminController;

    public ShowAllDoctorsCommand(AdminController adminController) {
        this.adminController = adminController;
    }

    @Override
    public void execute() {
        adminController.showAllDoctors();
    }
}