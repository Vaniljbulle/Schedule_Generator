package com.SGA;

import java.io.File;

public class Scheduler {
    private String participantsRaw;
    Athlete[] athletes;

    // Constructor
    public Scheduler() {
    }

    // Generate schedule
    public void generateSchedule(String inputCsv) {
        loadParticipants(inputCsv);
        initializeAthletes();
    }

    private void initializeAthletes() {
        // Loop through participants raw data
        String[] participants = participantsRaw.split("\n");
        athletes = new Athlete[participants.length];
        for (int i = 0; i < participants.length; i++) {
            athletes[i] = new Athlete(participants[i]);
        }
    }

    // Load participants data from csv file
    private void loadParticipants(String filePath) {
        FileHandler fileHandler = new FileHandler();
        fileHandler.readFileFromPath(filePath);
        participantsRaw = fileHandler.getFileContent();
    }


}
