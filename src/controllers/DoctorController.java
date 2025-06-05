package controllers;

import models.Appointment;
import models.Doctor;
import models.Patient;
import models.enums.StatusEnum;
import repository.AppointmentRepository;
import repository.DoctorRepository;
import utils.ValidationUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class DoctorController {

    private final DoctorRepository doctorRepository = new DoctorRepository();
    private final AppointmentRepository appointmentRepository = new AppointmentRepository();

    public void start(Scanner scanner) {
        System.out.println("\n=== Вход като Лекар ===");

        Doctor loggedDoctor = null;

        while (loggedDoctor == null) {
            try {
                System.out.print("Въведете вашето ID: ");
                int doctorId = Integer.parseInt(scanner.nextLine());

                System.out.print("Въведете вашето първо име: ");
                String firstName = scanner.nextLine().trim();
                if (!ValidationUtil.isValidName(firstName)) {
                    System.out.println("Невалидно име.");
                    continue;
                }

                System.out.print("Въведете парола: ");
                String password = scanner.nextLine().trim();

                loggedDoctor = doctorRepository.findByIdNameAndPassword(doctorId, firstName, password);

                if (loggedDoctor == null) {
                    System.out.println("Невалидни данни. Опитайте отново.");
                }
            } catch (NumberFormatException e) {
                System.out.println("ID трябва да е число.");
            }
        }

        System.out.println("Успешен вход! Добре дошли, д-р " + loggedDoctor.getLastName());

        showMenu(scanner, loggedDoctor);
    }

    private void showAppointmentsByStatus(Doctor doctor, StatusEnum statusEnum) {
        List<Appointment> appointments = appointmentRepository
                .getAppointmentsByDoctorIdAndStatus(doctor.getId(), statusEnum);

        if (appointments.isEmpty()) {
            System.out.println("Няма " + statusEnum.getName().toLowerCase() + " часове.");
            return;
        }

        System.out.println("\nЧасове със статус: " + statusEnum.getName());
        for (Appointment app : appointments) {
            System.out.println("Час ID: " + app.getId());
            System.out.println("Пациент: " + app.getPatient().getFirstName() + " " + app.getPatient().getLastName());
            System.out.println("Дата/час: " + app.getDateTime());
            System.out.println("Преглед: " + app.getExaminationType().getExaminationTypeEnum().getName());
            System.out.println("-----------------------------");
        }
    }

    private void showAppointmentsToday(Doctor doctor) {
        List<Appointment> appointments = appointmentRepository.getAppointmentsForTodayByDoctorId(doctor.getId());

        if (appointments.isEmpty()) {
            System.out.println("Нямате записани часове за днес.");
            return;
        }

        System.out.println("\nЧасове за днешния ден:");
        for (Appointment app : appointments) {
            System.out.println("Час ID: " + app.getId());
            System.out.println("Час: " + app.getDateTime());
            System.out.println("Пациент: " + app.getPatient().getFirstName() + " " + app.getPatient().getLastName());
            System.out.println("Статус: " + app.getStatus().getStatusEnum().getName());
            System.out.println("-------------------------");
        }
    }

    private void showTodayPatients(Doctor doctor) {
        List<Appointment> appointments = appointmentRepository.getAppointmentsForTodayByDoctorId(doctor.getId());

        if (appointments.isEmpty()) {
            System.out.println("Нямате пациенти за днес.");
            return;
        }

        System.out.println("\nПациенти с часове за днешния ден:");
        Set<Integer> shownIds = new HashSet<>();

        for (Appointment app : appointments) {
            var patient = app.getPatient();
            if (!shownIds.contains(patient.getId())) {
                System.out.println("Пациент ID: " + patient.getId());
                System.out.println("Име: " + patient.getFirstName() + " " + patient.getLastName());
                System.out.println("Възраст: " + patient.getAge());
                System.out.println("Имейл: " + patient.getEmail());
                System.out.println("Телефон: " + patient.getPhoneNumber());
                System.out.println("-----------------------------");
                shownIds.add(patient.getId());
            }
        }
    }

    private void showMenu(Scanner scanner, Doctor doctor) {
        while (true) {
            System.out.println("\n=== Меню за Лекар ===");
            System.out.println("1. Преглед на предстоящи часове");
            System.out.println("2. Преглед на отминали часове");
            System.out.println("3. Преглед на отменени часове");
            System.out.println("4. Преглед на всички часове за деня");
            System.out.println("5. Преглед на информация за пациентите през деня");
            System.out.println("6. Отмени час");
            System.out.println("0. Назад към главното меню");
            System.out.print("Избор: ");

            String input = scanner.nextLine();

            switch (input) {
                case "1": showAppointmentsByStatus(doctor, StatusEnum.UPCOMING); break;
                case "2": showAppointmentsByStatus(doctor, StatusEnum.PAST); break;
                case "3": showAppointmentsByStatus(doctor, StatusEnum.CANCELED); break;
                case "4": showAppointmentsToday(doctor); break;
                case "5": showTodayPatients(doctor); break;
                case "6": cancelAppointmentByDoctor(scanner, doctor); break;
                case "0": return;
                default: System.out.println("Невалиден избор. Опитайте отново.");
            }
        }
    }

    private void cancelAppointmentByDoctor(Scanner scanner, Doctor doctor) {
        List<Appointment> upcomingAppointments = appointmentRepository
                .getAppointmentsByDoctorIdAndStatus(doctor.getId(), StatusEnum.UPCOMING);

        if (upcomingAppointments.isEmpty()) {
            System.out.println("Нямате предстоящи часове за отмяна.");
            return;
        }

        System.out.println("\nПредстоящи часове:");
        for (Appointment app : upcomingAppointments) {
            System.out.println("Час ID: " + app.getId() +
                    ", Пациент: " + app.getPatient().getFirstName() + " " + app.getPatient().getLastName() +
                    ", Дата/час: " + app.getDateTime());
        }

        System.out.print("Въведете ID на часа за отмяна: ");
        try {
            int appointmentId = Integer.parseInt(scanner.nextLine());

            boolean found = upcomingAppointments.stream().anyMatch(a -> a.getId() == appointmentId);
            if (!found) {
                System.out.println("Невалидно ID. Няма такъв час сред предстоящите.");
                return;
            }

            appointmentRepository.updateAppointmentStatus(appointmentId, StatusEnum.CANCELED);
            System.out.println("Часът беше успешно отменен.");
        } catch (NumberFormatException e) {
            System.out.println("Моля, въведете валидно числово ID.");
        }
    }
}
