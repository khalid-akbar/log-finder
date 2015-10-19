package net.therap.logfinder;

import java.util.Scanner;

/**
 * @author babar
 * @since 10/15/15
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Enter log file name to be parsed: ");

        Scanner in = new Scanner(System.in);
        String logFileName = in.nextLine();

        LogFinder lf = new LogFinder(logFileName);

        int userChoice = lf.getUserChoice();
        lf.startFinding(userChoice);
    }
}
