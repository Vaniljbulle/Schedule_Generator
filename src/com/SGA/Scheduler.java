package com.SGA;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

public class Scheduler {
    HashMap<AgeGroup, HashMap<SexCategory, HashMap<CompetitionType, Competition>>> competitions = new HashMap<>();
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
        fillCompetitions();

    }

    // Creates and returns a competition of competitionType "type"
    private Competition createCompetitionOfType(CompetitionType type) {
        return switch (type) {
            case RUNNING60 -> new Running60();
            case RUNNING200 -> new Running200();
            case RUNNING800 -> new Running800();
            case RUNNING1500 -> new Running1500();
            case RUNNING3000 -> new Running3000();
            case HURDLES -> new RunningHurdles();
            case LONGJUMPING -> new JumpingLong();
            case TRIPLEJUMPING -> new JumpingTriple();
            case HIGHJUMPING -> new JumpingHigh();
            case POLEJUMPING -> new JumpingPole();
            case THROWING -> new Throwing();
        };
    }

    // Returns the age group of a participant
    // Only accurate if the age is not under 7
    private AgeGroup getAgeGroup(int age) {
        return switch (age) {
            case 7, 8 -> AgeGroup.age7_8;
            case 9, 10 -> AgeGroup.age9_10;
            case 11, 12 -> AgeGroup.age11_12;
            case 13, 14 -> AgeGroup.age13_14;
            case 15, 16 -> AgeGroup.age15_16;
            case 17, 18 -> AgeGroup.age17_18;
            default -> AgeGroup.ageAdult;
        };
    }

    private void addCompetitor(AgeGroup age, CompetitionType type, SexCategory sex, int athleteID) {
        if (competitions.containsKey(age)) {
            if (competitions.get(age).containsKey(sex)) {
                if (competitions.get(age).get(sex).containsKey(type)) {
                    competitions.get(age).get(sex).get(type).athleteIds.add(athleteID);
                    return;
                } else {
                    competitions.get(age).get(sex).put(type, createCompetitionOfType(type));
                }
            } else {
                competitions.get(age).put(sex, new HashMap<>());
            }
        } else {
            competitions.put(age, new HashMap<>());
        }
        addCompetitor(age, type, sex, athleteID);
    }

    // Fills the hashmap with competitions and sorts the athletes'
    // id's in the right age group, sex category and competition type
    private void fillCompetitions() {
        for (int i = 0; i < athletes.length; i++) {
            Athlete athlete = athletes[i];
            // Competitor is too young
            if (athlete.getAge() < 7) continue;

            // Getting the previous results from the competitor
            HashMap<CompetitionType, Double> results = athlete.getCompetitionResults();
            for (CompetitionType key : CompetitionType.values()) {
                Double result = results.get(key);
                // -1 means they do not compete in this competition
                if (result != -1) {
                    int age = athlete.getAge();
                    AgeGroup ageGroup = getAgeGroup(age);
                    if (athlete.getAge() < 15) {
                        addCompetitor(ageGroup, key, SexCategory.Both, i);
                    } else {
                        addCompetitor(ageGroup, key, athlete.getSex(), i);
                    }
                }
            }
        }
    }

    // Load participants data from csv file
    private String loadParticipants(String filePath) {
        FileHandler fileHandler = new FileHandler();
        fileHandler.readFileFromPath(filePath);
        return fileHandler.getFileContent();
    }


}
