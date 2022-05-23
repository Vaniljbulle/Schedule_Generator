package com.SGA;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();
        /*
        // API Input and output
        Scanner input = new Scanner(System.in);
        System.out.println("\n\n\n======================= SGA SCHEDULE GENERATOR =======================");
        System.out.println("\nPlease, enter the name of the input data file without any extension: ");
        String fileName = input.nextLine();

        System.out.println("Please, enter the name of the output file: ");
        String destFile = input.nextLine();
        System.out.println("\n======================================================================\n\n\n");
        */
        scheduler.generateSchedule("registration-list.csv", "out.csv");
    }
}
