package com.airtribe.meditrack.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class CSVUtil {

    private CSVUtil() {
        // Utility class
    }

    public static void write(String filePath, List<String> lines) {

        Validator.requireNonBlank(filePath, "File path");
        Validator.requireNotNull(lines, "Lines");

        Path path = Paths.get(filePath);

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {

            for (String line : lines) {

                writer.write(line == null ? "" : line);

                writer.newLine();
            }

        } catch (IOException e) {

            throw new IllegalStateException("Unable to write CSV file: " + filePath, e);
        }
    }

    public static void append(String filePath, String line) {

        Validator.requireNonBlank(filePath, "File path");

        Path path = Paths.get(filePath);

        try (BufferedWriter writer = Files.newBufferedWriter(path, java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND)) {

            writer.write(line == null ? "" : line);

            writer.newLine();

        } catch (IOException e) {

            throw new IllegalStateException("Unable to append to CSV file: " + filePath, e);
        }
    }

    public static List<String> read(String filePath) {

        Validator.requireNonBlank(filePath, "File path");

        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            return new ArrayList<>();
        }

        try {

            return Files.readAllLines(path);

        } catch (IOException e) {

            throw new IllegalStateException("Unable to read CSV file: " + filePath, e);
        }
    }

    public static boolean exists(String filePath) {

        Validator.requireNonBlank(filePath, "File path");

        return Files.exists(Paths.get(filePath));
    }

    public static void delete(String filePath) {

        Validator.requireNonBlank(filePath, "File path");

        try {

            Files.deleteIfExists(Paths.get(filePath));

        } catch (IOException e) {

            throw new IllegalStateException("Unable to delete CSV file: " + filePath, e);
        }
    }
}