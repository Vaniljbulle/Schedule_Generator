package com.SGA;

import java.util.Vector;

public class Main {

    public static void main(String[] args) {
        Vector<Competition> competitions = new Vector<>();

        competitions.add(new PoleJumping());
        competitions.add(new Running60());
        competitions.add(new Throwing());

        for (Competition tmp: competitions) {
            tmp.getNext();
        }

        Generator generator = new Generator();
        generator.generate();
        //Scheduler scheduler = new Scheduler();
        //scheduler.generateSchedule("registration-list.csv");

        //testFileHandler();
    }

    // Not a unit test, but a test to see if the file handler works
    private static void testFileHandler() {
        FileHandler fileHandler = new FileHandler();
        fileHandler.readFileFromPath("registration-list.csv");
        String raw = fileHandler.getFileContent();
        System.out.println(raw);

        fileHandler.writeFileToPath("out.csv", "test");
        fileHandler.writeFileToPath("duplicate.csv", raw);

    }


}
