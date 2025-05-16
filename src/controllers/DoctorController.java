package controllers;

import models.Appointment;
import models.Doctor;
import models.Patient;
import models.enums.StatusEnum;
import repository.AppointmentRepository;
import repository.DoctorRepository;

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
            System.out.print("Въведете вашето ID: ");
            int doctorId = Integer.parseInt(scanner.nextLine());

            System.out.print("Въведете вашето първо име: ");
            String firstName = scanner.nextLine().trim();

            System.out.print("Въведете парола: ");
            String password = scanner.nextLine().trim();

            loggedDoctor = doctorRepository.findByIdNameAndPassword(doctorId, firstName, password);

            if (loggedDoctor == null) {
                System.out.println("Невалидни данни. Опитайте отново.");
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
            System.out.println("0. Назад към главното меню");
            System.out.print("Избор: ");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    showAppointmentsByStatus(doctor, StatusEnum.UPCOMING);
                    break;
                case "2":
                    showAppointmentsByStatus(doctor, StatusEnum.PAST);
                    break;
                case "3":
                    showAppointmentsByStatus(doctor, StatusEnum.CANCELED);
                    break;
                case "4":
                    showAppointmentsToday(doctor);
                    break;
                case "5":
                    showTodayPatients(doctor);
                    break;
                case "0":
                    continue;
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
    // TODO: Доктора да отменя часовее
}
