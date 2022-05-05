package com.SGA;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

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

    private void fillCompetitions() {

        HashMap<AgeGroup, HashMap<SexCategory, Competition>> competitions = new HashMap<>();

        HashMap<SexCategory, Competition> tmp = new HashMap<>();
        tmp.put(SexCategory.Male, new PoleJumping());
        competitions.put(AgeGroup.age9_10, tmp);

        for (HashMap<SexCategory, Competition> i: competitions.values()) {
            for (Competition j : i.values()) {
                j.getNext();
            }
        }
    }

    // Load participants data from csv file
    private void loadParticipants(String filePath) {
        FileHandler fileHandler = new FileHandler();
        fileHandler.readFileFromPath(filePath);
        participantsRaw = fileHandler.getFileContent();
    }


}
