package views;

import commands.Command;
import commands.main.MainMenuCommandFactory;
import java.util.Scanner;

public class MainMenuView {

    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        MainMenuCommandFactory factory = new MainMenuCommandFactory(scanner);

        while (true) {
            System.out.println("\n=== Болнична Информационна Система ===");
            System.out.println("Моля, изберете роля:");
            System.out.println("1. Вход като Пациент");
            System.out.println("2. Вход като Лекар");
            System.out.println("3. Вход като Администратор");
            System.out.println("0. Изход");

            System.out.print("Избор: ");
            String input = scanner.nextLine().trim();

            if ("0".equals(input)) {
                Command exitCommand = factory.getCommand(input);
                if (exitCommand != null) {
                    exitCommand.execute();
                }
                return;
            }

            Command command = factory.getCommand(input);
            if (command != null) {
                command.execute();
            } else {
                System.out.println("Невалиден избор. Опитайте отново.");
            }
        }
    }
}