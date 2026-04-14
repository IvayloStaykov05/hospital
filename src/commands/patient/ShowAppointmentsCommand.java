package commands.patient;

import commands.Command;
import controllers.PatientController;
import models.Patient;

public class ShowAppointmentsCommand implements Command {
    private final PatientController patientController;
    private final Patient patient;

    public ShowAppointmentsCommand(PatientController patientController, Patient patient) {
        this.patientController = patientController;
        this.patient = patient;
    }

    @Override
    public void execute() {
        patientController.showAppointments(patient);
    }
}