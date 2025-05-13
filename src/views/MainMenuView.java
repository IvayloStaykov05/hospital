package views;

import controllers.AdminController;
import controllers.DoctorController;
import controllers.PatientController;

import java.util.Scanner;

public class MainMenuView {

    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\n=== Болнична Информационна Система ===");
            System.out.println("Моля, изберете роля:");
            System.out.println("1. Вход като Пациент");
            System.out.println("2. Вход като Лекар");
            System.out.println("3. Вход като Администратор");
            System.out.println("0. Изход");

            System.out.print("Избор: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    // TODO: new PatientController().start(scanner);
                    break;
                case "2":
                    new DoctorController().start(scanner);
                    break;
                case "3":
                    // TODO: new AdminController().start(scanner);
                    break;
                case "0":
                    System.out.println("Изход от системата. Довиждане!");
                    return;
                default:
                    System.out.println("Невалиден избор. Опитайте отново.");
            }
        }
    }
}