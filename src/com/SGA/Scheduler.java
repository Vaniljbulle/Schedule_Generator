package com.SGA;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

public class Scheduler {
    HashMap<AgeGroup, HashMap<SexCategory, Competition>> competitions = new HashMap<>();
    private Athlete[] athletes;

    // Constructor
    public Scheduler() {
    }

    // Generate schedule
    public void generateSchedule(String filePath) {
        initializeAthletes(filePath);
    }

    private void initializeAthletes(String filePath) {
        // Load participants raw data and split it into lines
        String participantsRawData = loadParticipants(filePath);
        String[] participants = participantsRawData.split("\n");

        // Create athletes array and fill it with participants
        athletes = new Athlete[participants.length];
        for (int i = 0; i < participants.length; i++) {
            athletes[i] = new Athlete(participants[i]);
        }
    }

    private void fillCompetitions() {
        for (Athlete athlete : athletes) {
            // Competitor is too young
            if (athlete.getAge() < 7) continue;

            HashMap<SexCategory, Competition> tmp = new HashMap<>();
            if (athlete.getAge() < 15) {
                tmp.put(SexCategory.Both, new PoleJumping());

            }
            tmp.put(SexCategory.Male, new PoleJumping());
            competitions.put(AgeGroup.age9_10, tmp);
        }
    }

    // Load participants data from csv file
    private String loadParticipants(String filePath) {
        FileHandler fileHandler = new FileHandler();
        fileHandler.readFileFromPath(filePath);
        return fileHandler.getFileContent();
    }


}
