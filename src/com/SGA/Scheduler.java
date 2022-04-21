package com.SGA;

import java.io.File;

public class Scheduler {
    private String participantsRaw;

    // Constructor
    public Scheduler() {
    }

    // Generate schedule
    public void generateSchedule(String inputCsv) {
        loadParticipants(inputCsv);
    }

    // Load participants data from csv file
    private void loadParticipants(String filePath) {
        FileHandler fileHandler = new FileHandler();
        fileHandler.readFileFromPath(filePath);
        participantsRaw = fileHandler.getFileContent();
    }


}
