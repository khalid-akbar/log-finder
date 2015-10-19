package net.therap.logfinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author babar
 * @since 10/15/15
 */
public class LogFinder {

    private Scanner reader;
    private Scanner in;
    private LogFinderService service;

    public LogFinder(String fileName) {
        File file = new File(fileName);

        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            System.exit(789);
        }

        in = new Scanner(System.in);
        service = new LogFinderService();
    }

    public int getUserChoice() {
        System.out.println("Enter an Option: ");
        System.out.println("Option #1. Print Hourly Usage Data");
        System.out.println("Option #2. Enter User Id to find total time spent today on Therap server");
        System.out.println("Option #3. Enter Uri to find total hit statistics");

        int choice = in.nextInt();
        in.nextLine();

        return choice;
    }

    public void startFinding(int choice) {
        switch (choice) {
            case 1:
                printHourlyUsageData();
                break;
            case 2:
                printUserTimeOnServer();
                break;
            case 3:
                printUrlHitStatistics();
                break;
        }
    }

    private void printHourlyUsageData() {
        int currentHour = 0;

        System.out.println(service.getHourlyFormattedString(currentHour));

        while (reader.hasNextLine()) {
            String line = reader.nextLine();

            if (line.contains("PROFILER")) {
                int hour = service.getHour(line);

                if (hour > currentHour) {
                    currentHour = hour;
                    service.getHourlyFormattedString(currentHour);
                }

                System.out.println(service.getUserName(line) + "\n" +
                        service.getRequestType(line) + "\n" +
                        service.getServerTime(line) + "ms\n");
            }
        }
    }

    private void printUserTimeOnServer() {
        System.out.println("Enter user id for query: ");

        String userId = in.nextLine();

        long totalTime = 0;

        while (reader.hasNextLine()) {
            String line = reader.nextLine();

            if (line.contains("PROFILER") && line.contains(" U:" + userId + " ")) {
                totalTime += service.getServerTime(line);
            }
        }

        System.out.println("Total Time: " + service.timeFormat(totalTime, LogFinderService.TimeFormat.MINUTE));
    }

    private void printUrlHitStatistics() {
        System.out.println("Enter uri for query :");

        String uri = in.nextLine();

        int currentHour = 0;

        long totalHourlyPostTime = 0;
        long totalHourlyGetTime = 0;

        System.out.println("Hour: " + currentHour);

        while (reader.hasNextLine()) {
            String line = reader.nextLine();

            if (line.contains("PROFILER") && line.contains(uri)) {
                int time = service.getServerTime(line);

                if (service.getRequestType(line).equals("POST")) {
                    totalHourlyPostTime += time;
                } else {
                    totalHourlyGetTime += time;
                }

                int hour = service.getHour(line);
                if (hour > currentHour) {
                    System.out.println("Total POST Request Time: " +
                            service.timeFormat(totalHourlyPostTime, LogFinderService.TimeFormat.MINUTE) +
                            "\nTotal GET Request Time: " +
                            service.timeFormat(totalHourlyGetTime, LogFinderService.TimeFormat.MINUTE) + "\n\n");

                    currentHour = hour;
                    totalHourlyGetTime = 0;
                    totalHourlyPostTime = 0;

                    System.out.println(service.getHourlyFormattedString(currentHour));
                }
            }
        }
    }
}
