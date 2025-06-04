package utils;

import java.io.IOException;
import java.util.logging.*;

public class LoggerConfig {
    private static final String LOG_FILE = "logs/app.log";
    private static boolean initialized = false;

    public static void configureLogger(Logger logger) {
        if (initialized) return;

        try {
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get("logs"));

            FileHandler fileHandler = new FileHandler(LOG_FILE, true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.SEVERE);

            logger.addHandler(fileHandler);
            logger.addHandler(consoleHandler);
            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);

            initialized = true;

        } catch (IOException e) {
            System.err.println("Неуспешна инициализация на Logger: " + e.getMessage());
        }
    }
}
