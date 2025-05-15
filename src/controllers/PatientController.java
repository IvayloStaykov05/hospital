package controllers;

import models.Appointment;
import models.Doctor;
import models.Patient;
import models.enums.ExaminationTypeEnum;
import models.enums.StatusEnum;
import repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class PatientController {
    private final PatientRepository patientRepository = new PatientRepository();
    private final DoctorRepository doctorRepository = new DoctorRepository();
    private final AppointmentRepository appointmentRepository = new AppointmentRepository();
    private final StatusRepository statusRepository = new StatusRepository();
    private final ExaminationTypeRepository examinationTypeRepository = new ExaminationTypeRepository();

    public void start(Scanner scanner) {
        System.out.println("\n=== Вход като Пациент ===");

        Patient loggedPatient = null;

        while (loggedPatient == null) {
            try {
                System.out.print("Въведете вашето ID: ");
                int id = Integer.parseInt(scanner.nextLine());

                System.out.print("Въведете вашето първо име: ");
                String firstName = scanner.nextLine().trim();

                loggedPatient = patientRepository.findByIdAndFirstName(id, firstName);
                if (loggedPatient == null) {
                    System.out.println("Невалидни данни. Опитайте отново.");
                }
            } catch (NumberFormatException e) {
                System.out.println("ID трябва да е число.");
            }
        }

        System.out.println("Здравейте, " + loggedPatient.getFirstName() + "!");

        showMenu(scanner, loggedPatient);
    }

    private void showMenu(Scanner scanner, Patient patient) {
        while (true) {
            System.out.println("\n=== Меню за Пациент ===");
            System.out.println("1. Преглед на записани часове");
            System.out.println("2. Запазване на нов час");
            System.out.println("3. Промяна на дата/час на записан час");
            System.out.println("4. Отказване от записан час");
            System.out.println("0. Назад към главното меню");
            System.out.print("Избор: ");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    showAppointments(patient);
                    break;
                case "2":
                    bookAppointment(scanner, patient);
                    break;
                case "3":
                    updateAppointment(scanner);
                    break;
                case "4":
                    cancelAppointment(scanner);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Невалиден избор. Опитайте отново.");
            }
        }
    }

    private void showAppointments(Patient patient) {
        List<Appointment> appointments = appointmentRepository.getAppointmentsByPatientId(patient.getId());

        if (appointments.isEmpty()) {
            System.out.println("Нямате записани часове.");
            return;
        }

        appointments.sort((a1, a2) -> a1.getDateTime().compareTo(a2.getDateTime()));

        for (Appointment app : appointments) {
            System.out.println("Час ID: " + app.getId());
            System.out.println("Дата/час: " + app.getDateTime());
            System.out.println("Лекар: " + app.getDoctor().getFirstName() + " " + app.getDoctor().getLastName());
            System.out.println("Преглед: " + app.getExaminationType().getExaminationTypeEnum().getName());
            System.out.println("Статус: " + app.getStatus().getStatusEnum().getName());
            System.out.println("-------------------------");
        }
    }

    private void bookAppointment(Scanner scanner, Patient patient) {
        System.out.print("Въведете ID на лекар: ");
        int doctorId = Integer.parseInt(scanner.nextLine());

        Doctor doctor = doctorRepository.getDoctorById(doctorId);
        if (doctor == null) {
            System.out.println("Лекар с такова ID не съществува.");
            return;
        }

        System.out.println("Изберете тип преглед:");
        for (ExaminationTypeEnum type : ExaminationTypeEnum.values()) {
            System.out.println(type.ordinal() + 1 + ". " + type.getName());
        }
        int typeChoice = Integer.parseInt(scanner.nextLine());

        if (typeChoice < 1 || typeChoice > ExaminationTypeEnum.values().length) {
            System.out.println("Невалиден избор на тип преглед.");
            return;
        }

        ExaminationTypeEnum selectedType = ExaminationTypeEnum.values()[typeChoice - 1];

        System.out.print("Въведете дата и час във формат (YYYY-MM-DDTHH:MM): ");
        LocalDateTime dateTime = LocalDateTime.parse(scanner.nextLine());

        Appointment appointment = new Appointment(
                0,
                patient,
                doctor,
                examinationTypeRepository.getExaminationTypeById(selectedType.ordinal() + 1),
                dateTime,
                statusRepository.getStatusByEnum(StatusEnum.UPCOMING)
        );

        appointmentRepository.insertAppointment(appointment);
    }

    private void updateAppointment(Scanner scanner) {
        System.out.print("Въведете ID на часа, който искате да промените: ");
        int appointmentId = Integer.parseInt(scanner.nextLine());

        System.out.print("Въведете нова дата и час (YYYY-MM-DDTHH:MM): ");
        LocalDateTime newDateTime = LocalDateTime.parse(scanner.nextLine());

        System.out.println("Сигурни ли сте, че искате да промените този час? (да/не): ");
        if (!scanner.nextLine().equalsIgnoreCase("да")) {
            System.out.println("Операцията е отказана.");
            return;
        }

        appointmentRepository.updateAppointmentDateTime(appointmentId, newDateTime);
    }

    private void cancelAppointment(Scanner scanner) {
        System.out.print("Въведете ID на часа, който искате да отмените: ");
        int appointmentId = Integer.parseInt(scanner.nextLine());

        System.out.println("Сигурни ли сте, че искате да отмените този час? (да/не): ");
        if (!scanner.nextLine().equalsIgnoreCase("да")) {
            System.out.println("Операцията е отказана.");
            return;
        }

        appointmentRepository.updateAppointmentStatus(appointmentId, StatusEnum.CANCELED);
    }
}
