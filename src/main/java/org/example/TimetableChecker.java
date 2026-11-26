package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimetableChecker {

    private static final String FILE_NAME = "lessons.txt";
    private static final Pattern LINE_PATTERN = Pattern.compile(
            "^(MON|TUE|WED|THU|FRI) ([01]\\d|2[0-3]):([0-5]\\d)-([01]\\d|2[0-3]):([0-5]\\d) (\\d{3}) ([A-Z][A-Za-z]+(?: [A-Za-z]+)*)$"
    );

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static boolean isValidLine(String line) {
        if (line == null) {
            return false;
        }

        Matcher matcher = LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            return false;
        }

        String startHour = matcher.group(2);
        String startMinute = matcher.group(3);
        String endHour = matcher.group(4);
        String endMinute = matcher.group(5);

        try {
            LocalTime startTime = LocalTime.parse(startHour + ":" + startMinute, TIME_FORMATTER);
            LocalTime endTime = LocalTime.parse(endHour + ":" + endMinute, TIME_FORMATTER);
            return endTime.isAfter(startTime);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String status = isValidLine(line) ? "VALID" : "INVALID";
                System.out.println(status + " -> " + line);
            }
        } catch (IOException e) {
            System.out.println("Error reading " + FILE_NAME + ": " + e.getMessage());
        }
    }
}