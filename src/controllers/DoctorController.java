package controllers;

import models.Doctor;
import repository.DoctorRepository;

import java.util.Scanner;

public class DoctorController {

    private final DoctorRepository doctorRepository = new DoctorRepository();

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

    private void showMenu(Scanner scanner, Doctor doctor) {
        while (true) {
            System.out.println("\n=== Меню за Лекар ===");
            System.out.println("1. Преглед на предстоящи часове");
            System.out.println("2. Преглед на отминали часове");
            System.out.println("3. Преглед на отменени часове");
            System.out.println("4. Преглед на информация за пациенти");
            System.out.println("0. Назад към главното меню");
            System.out.print("Избор: ");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    // TODO: Показване на предстоящи часове
                    break;
                case "2":
                    // TODO: Показване на отминали часове
                    break;
                case "3":
                    // TODO: Показване на отменени часове
                    break;
                case "4":
                    // TODO: Показване на информация за пациентите
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Невалиден избор. Опитайте отново.");
            }
        }
    }
}
