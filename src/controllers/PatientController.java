package controllers;

import models.Appointment;
import models.Doctor;
import models.Patient;
import models.enums.ExaminationTypeEnum;
import models.enums.StatusEnum;
import repository.*;
import utils.ValidationUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
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

        String response = "";
        while (!response.equalsIgnoreCase("да") && !response.equalsIgnoreCase("не")) {
            System.out.print("Регистрирани ли сте вече в системата? (да/не): ");
            response = scanner.nextLine().trim().toLowerCase();
            if (!response.equals("да") && !response.equals("не")) {
                System.out.println("Моля, въведете само 'да' или 'не'.");
            }
        }

        if (response.equals("не")) {
            loggedPatient = addPatient(scanner);
            System.out.println("Успешна регистрация. Вече сте влезли като: " + loggedPatient.getFirstName());
        } else {
            while (loggedPatient == null) {
                try {
                    System.out.print("Въведете вашето ID: ");
                    int id = Integer.parseInt(scanner.nextLine());

                    System.out.print("Въведете вашето първо име: ");
                    String firstName = scanner.nextLine().trim();

                    System.out.print("Въведете парола: ");
                    String password = scanner.nextLine().trim();

                    loggedPatient = patientRepository.findByIdAndFirstName(id, firstName, password);
                    if (loggedPatient == null) {
                        System.out.println("Невалидни данни. Опитайте отново!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("ID трябва да е число.");
                }
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
                case "1": showAppointments(patient); break;
                case "2": bookAppointment(scanner, patient); break;
                case "3": updateAppointment(scanner); break;
                case "4": cancelAppointment(scanner); break;
                case "0": return;
                default: System.out.println("Невалиден избор.");
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
        try {
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
                System.out.println("Невалиден избор.");
                return;
            }

            ExaminationTypeEnum selectedType = ExaminationTypeEnum.values()[typeChoice - 1];

            System.out.print("Въведете дата и час (формат YYYY-MM-DDTHH:MM): ");
            LocalDateTime dateTime = LocalDateTime.parse(scanner.nextLine());

            if (!appointmentRepository.isDoctorAvailable(doctorId, dateTime)) {
                System.out.println("Лекарят вече има записан час по това време.");
                return;
            }

            Appointment appointment = new Appointment(
                    0,
                    patient,
                    doctor,
                    examinationTypeRepository.getExaminationTypeById(selectedType.ordinal() + 1),
                    dateTime,
                    statusRepository.getStatusByEnum(StatusEnum.UPCOMING)
            );

            appointmentRepository.insertAppointment(appointment);
        } catch (NumberFormatException e) {
            System.out.println("Моля, въведете валидни числови стойности.");
        } catch (DateTimeParseException e) {
            System.out.println("Невалиден формат на дата/час.");
        }
    }

    private void updateAppointment(Scanner scanner) {
        try {
            System.out.print("Въведете ID на часа, който искате да промените: ");
            int appointmentId = Integer.parseInt(scanner.nextLine());

            Appointment appointment = appointmentRepository.getAppointmentById(appointmentId);
            if (appointment == null) {
                System.out.println("Часът не съществува.");
                return;
            }

            System.out.print("Въведете нова дата и час (YYYY-MM-DDTHH:MM): ");
            LocalDateTime newDateTime = LocalDateTime.parse(scanner.nextLine());

            if (!appointmentRepository.isDoctorAvailable(appointment.getDoctor().getId(), newDateTime)) {
                System.out.println("Лекарят вече има друг час по това време.");
                return;
            }

            System.out.println("Сигурни ли сте, че искате да промените този час? (да/не): ");
            if (!scanner.nextLine().equalsIgnoreCase("да")) {
                System.out.println("Операцията е отказана.");
                return;
            }

            appointmentRepository.updateAppointmentDateTime(appointmentId, newDateTime);
        } catch (Exception e) {
            System.out.println("Невалиден вход. Моля, опитайте отново.");
        }
    }

    private void cancelAppointment(Scanner scanner) {
        try {
            System.out.print("Въведете ID на часа, който искате да отмените: ");
            int appointmentId = Integer.parseInt(scanner.nextLine());

            System.out.println("Сигурни ли сте, че искате да отмените този час? (да/не): ");
            if (!scanner.nextLine().equalsIgnoreCase("да")) {
                System.out.println("Операцията е отказана.");
                return;
            }

            appointmentRepository.updateAppointmentStatus(appointmentId, StatusEnum.CANCELED);
        } catch (NumberFormatException e) {
            System.out.println("ID трябва да е число.");
        }
    }

    private Patient addPatient(Scanner scanner) {
        System.out.println("\n=== Добавяне на нов пациент ===");

        System.out.print("Име: ");
        String firstName = scanner.nextLine();
        if (!ValidationUtil.isValidName(firstName)) {
            System.out.println("Невалидно име.");
            return null;
        }

        System.out.print("Фамилия: ");
        String lastName = scanner.nextLine();
        if (!ValidationUtil.isValidName(lastName)) {
            System.out.println("Невалидна фамилия.");
            return null;
        }

        System.out.print("Възраст: ");
        int age = Integer.parseInt(scanner.nextLine());
        if (age < 0 || age > 120) {
            System.out.println("Невалидна възраст.");
            return null;
        }

        System.out.print("Имейл: ");
        String email = scanner.nextLine();
        if (!ValidationUtil.isValidEmail(email)) {
            System.out.println("Невалиден имейл.");
            return null;
        }

        System.out.print("Телефон: ");
        String phone = scanner.nextLine();
        if (!ValidationUtil.isValidPhone(phone)) {
            System.out.println("Невалиден телефон.");
            return null;
        }

        System.out.print("Парола: ");
        String password = scanner.nextLine();
        if (password.length() < 6) {
            System.out.println("Паролата трябва да е поне 6 символа.");
            return null;
        }

        Patient newPatient = new Patient(0, firstName, lastName, email, phone, password, age);
        patientRepository.insertPatient(newPatient);

        System.out.println("Пациентът беше успешно добавен.");

        return newPatient;
    }
}
