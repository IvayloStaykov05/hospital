package controllers;

import models.Doctor;
import models.Patient;
import models.Specialty;
import models.enums.SpecialtyEnum;
import repository.DoctorRepository;
import repository.PatientRepository;
import repository.SpecialtyRepository;
import utils.ValidationUtil;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class AdminController {
    private final DoctorRepository doctorRepository = new DoctorRepository();
    private final PatientRepository patientRepository = new PatientRepository();
    private final SpecialtyRepository specialtyRepository = new SpecialtyRepository();

    private String ADMIN_USERNAME;
    private String ADMIN_PASSWORD;

    public AdminController() {
        loadAdminCredentials();
    }

    private void loadAdminCredentials() {
        try (Scanner fileScanner = new Scanner(new File("admin_credentials.txt"))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.startsWith("username=")) {
                    ADMIN_USERNAME = line.substring("username=".length()).trim();
                } else if (line.startsWith("password=")) {
                    ADMIN_PASSWORD = line.substring("password=".length()).trim();
                }
            }
        } catch (Exception e) {
            System.err.println("Неуспешно зареждане на администраторските данни: " + e.getMessage());
        }
    }

    public void start(Scanner scanner) {
        System.out.println("\n=== Вход като Администратор ===");

        while (true) {
            System.out.print("Потребителско име: ");
            String username = scanner.nextLine();

            System.out.print("Парола: ");
            String password = scanner.nextLine();

            if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
                System.out.println("Успешен вход. Добре дошли, Администратор.");
                showMenu(scanner);
                return;
            } else {
                System.out.println("Невалидно потребителско име или парола.");
            }
        }
    }

    private void showMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Администраторско меню ===");
            System.out.println("1. Виж всички лекари");
            System.out.println("2. Добави нов лекар");
            System.out.println("3. Изтрий лекар");
            System.out.println("4. Виж всички пациенти");
            System.out.println("5. Промени данни за лекар");
            System.out.println("6. Търси пациент по име");
            System.out.println("7. Добави нов пациент");
            System.out.println("0. Изход");
            System.out.print("Избор: ");

            String input = scanner.nextLine();

            switch (input) {
                case "1": showAllDoctors(); break;
                case "2": addDoctor(scanner); break;
                case "3": deleteDoctor(scanner); break;
                case "4": showAllPatients(); break;
                case "5": editDoctor(scanner); break;
                case "6": searchPatients(scanner); break;
                case "7": addPatients(scanner); break;
                case "0": return;
                default: System.out.println("Невалиден избор.");
            }
        }
    }

    private void showAllDoctors() {
        List<Doctor> doctors = doctorRepository.getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("Няма налични лекари.");
            return;
        }
        for (Doctor d : doctors) {
            System.out.printf("ID: %d | %s %s | Имейл: %s | Тел: %s | Специалност: %s\n",
                    d.getId(), d.getFirstName(), d.getLastName(), d.getEmail(), d.getPhoneNumber(),
                    d.getSpecialty() != null ? d.getSpecialty().getSpecialtyEnum().getName() : "няма");
        }
    }

    private void addDoctor(Scanner scanner) {
        System.out.println("\n=== Добавяне на нов лекар ===");

        String firstName;
        do {
            System.out.print("Име: ");
            firstName = scanner.nextLine();
            if (!ValidationUtil.isValidName(firstName)) {
                System.out.println("Невалидно име. Опитайте отново.");
            }
        } while (!ValidationUtil.isValidName(firstName));

        String lastName;
        do {
            System.out.print("Фамилия: ");
            lastName = scanner.nextLine();
            if (!ValidationUtil.isValidName(lastName)) {
                System.out.println("Невалидна фамилия. Опитайте отново.");
            }
        } while (!ValidationUtil.isValidName(lastName));

        String email;
        do {
            System.out.print("Имейл: ");
            email = scanner.nextLine();
            if (!ValidationUtil.isValidEmail(email)) {
                System.out.println("Невалиден имейл. Опитайте отново.");
            }
        } while (!ValidationUtil.isValidEmail(email));

        String phone;
        do {
            System.out.print("Телефон: ");
            phone = scanner.nextLine();
            if (!ValidationUtil.isValidPhone(phone)) {
                System.out.println("Невалиден телефон. Опитайте отново.");
            }
        } while (!ValidationUtil.isValidPhone(phone));

        Specialty specialty = selectSpecialty(scanner);
        if (specialty == null) return;

        String password;
        do {
            System.out.print("Парола: ");
            password = scanner.nextLine();
            if (!ValidationUtil.isValidPassword(password)) {
                System.out.println("Паролата трябва да е поне 6 символа и да съдържа главна, малка буква и цифра. Опитайте отново.");
            }
        } while (!ValidationUtil.isValidPassword(password));

        Doctor doctor = new Doctor(0, firstName, lastName, email, phone, specialty, password);
        doctorRepository.insertDoctor(doctor);
        System.out.println("Лекарят беше успешно добавен.");
    }

    private void deleteDoctor(Scanner scanner) {
        System.out.print("ID на лекаря за изтриване: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Сигурни ли сте? (да/не): ");
        if (!scanner.nextLine().equalsIgnoreCase("да")) {
            System.out.println("Операцията отказана.");
            return;
        }

        doctorRepository.deleteDoctorById(id);
        System.out.println("Лекарят беше изтрит.");
    }

    private void showAllPatients() {
        List<Patient> patients = patientRepository.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("Няма пациенти.");
            return;
        }
        for (Patient p : patients) {
            System.out.printf("ID: %d | %s %s | Възраст: %d | Имейл: %s | Тел: %s\n",
                    p.getId(), p.getFirstName(), p.getLastName(), p.getAge(), p.getEmail(), p.getPhoneNumber());
        }
    }

    private void editDoctor(Scanner scanner) {
        System.out.print("ID на лекаря за редакция: ");
        int id = Integer.parseInt(scanner.nextLine());
        Doctor doctor = doctorRepository.getDoctorById(id);
        if (doctor == null) {
            System.out.println("Лекарят не е намерен.");
            return;
        }

        System.out.print("Ново име (" + doctor.getFirstName() + "): ");
        String input = scanner.nextLine();
        if (!input.isBlank() && ValidationUtil.isValidName(input)) doctor.setFirstName(input);

        System.out.print("Нова фамилия (" + doctor.getLastName() + "): ");
        input = scanner.nextLine();
        if (!input.isBlank() && ValidationUtil.isValidName(input)) doctor.setLastName(input);

        System.out.print("Нов имейл (" + doctor.getEmail() + "): ");
        input = scanner.nextLine();
        if (!input.isBlank() && ValidationUtil.isValidEmail(input)) doctor.setEmail(input);

        System.out.print("Нов телефон (" + doctor.getPhoneNumber() + "): ");
        input = scanner.nextLine();
        if (!input.isBlank() && ValidationUtil.isValidPhone(input)) doctor.setPhoneNumber(input);

        System.out.println("Ако желаете да смените специалността, изберете от списъка. Ако не, натиснете Enter:");
        for (SpecialtyEnum s : SpecialtyEnum.values()) {
            System.out.println((s.ordinal() + 1) + ". " + s.getName());
        }

        input = scanner.nextLine();
        if (!input.isBlank()) {
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= SpecialtyEnum.values().length) {
                    SpecialtyEnum selected = SpecialtyEnum.values()[choice - 1];
                    doctor.setSpecialty(new Specialty(choice, selected));
                } else {
                    System.out.println("Невалиден избор. Специалността остава старата.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Невалиден формат. Специалността остава старата.");
            }
        }

        System.out.print("Нова парола: ");
        input = scanner.nextLine();
        if (!input.isBlank() && ValidationUtil.isValidPassword(input)) doctor.setPassword(input);

        doctorRepository.updateDoctor(doctor);
        System.out.println("Лекарят беше обновен.");
    }

    private void searchPatients(Scanner scanner) {
        System.out.print("Въведете име или фамилия за търсене: ");
        String keyword = scanner.nextLine().trim().toLowerCase();

        List<Patient> matches = patientRepository.getAllPatients().stream()
                .filter(p -> p.getFirstName().toLowerCase().contains(keyword)
                        || p.getLastName().toLowerCase().contains(keyword))
                .toList();

        if (matches.isEmpty()) {
            System.out.println("Няма пациенти с такова име.");
        } else {
            matches.forEach(p -> System.out.printf("ID: %d | %s %s | Имейл: %s\n",
                    p.getId(), p.getFirstName(), p.getLastName(), p.getEmail()));
        }
    }

    private Specialty selectSpecialty(Scanner scanner) {
        System.out.println("Изберете специалност:");
        for (SpecialtyEnum s : SpecialtyEnum.values()) {
            System.out.println((s.ordinal() + 1) + ". " + s.getName());
        }

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Невалиден избор.");
            return null;
        }

        if (choice < 1 || choice > SpecialtyEnum.values().length) {
            System.out.println("Невалиден избор.");
            return null;
        }

        return new Specialty(choice, SpecialtyEnum.values()[choice - 1]);
    }

    public void addPatients(Scanner scanner) {
        System.out.println("\n=== Добавяне на нов пациент ===");

        String firstName;
        do {
            System.out.print("Име: ");
            firstName = scanner.nextLine();
            if (!ValidationUtil.isValidName(firstName)) {
                System.out.println("Невалидно име. Опитайте отново.");
            }
        } while (!ValidationUtil.isValidName(firstName));

        String lastName;
        do {
            System.out.print("Фамилия: ");
            lastName = scanner.nextLine();
            if (!ValidationUtil.isValidName(lastName)) {
                System.out.println("Невалидна фамилия. Опитайте отново.");
            }
        } while (!ValidationUtil.isValidName(lastName));

        int age = -1;
        while (age < 0 || age > 120) {
            System.out.print("Възраст: ");
            try {
                age = Integer.parseInt(scanner.nextLine());
                if (!ValidationUtil.isValidAge(age)) {
                    System.out.println("Невалидна възраст. Опитайте отново.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Моля, въведете валидна възраст.");
            }
        }

        String email;
        do {
            System.out.print("Имейл: ");
            email = scanner.nextLine();
            if (!ValidationUtil.isValidEmail(email)) {
                System.out.println("Невалиден имейл. Опитайте отново.");
            }
        } while (!ValidationUtil.isValidEmail(email));

        String phone;
        do {
            System.out.print("Телефон: ");
            phone = scanner.nextLine();
            if (!ValidationUtil.isValidPhone(phone)) {
                System.out.println("Невалиден телефон. Опитайте отново.");
            }
        } while (!ValidationUtil.isValidPhone(phone));

        String password;
        do {
            System.out.print("Парола: ");
            password = scanner.nextLine();
            if (!ValidationUtil.isValidPassword(password)) {
                System.out.println("Паролата трябва да е поне 6 символа и да съдържа главна, малка буква и цифра. Опитайте отново.");
            }
        } while (!ValidationUtil.isValidPassword(password));

        Patient newPatient = new Patient(0, firstName, lastName, email, phone, password, age);
        patientRepository.insertPatient(newPatient);

        System.out.println("Пациентът беше успешно добавен.");
    }
}
