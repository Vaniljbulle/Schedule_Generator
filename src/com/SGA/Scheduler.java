package com.SGA;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

public class Scheduler {
    HashMap<AgeGroup, HashMap<SexCategory, HashMap<CompetitionType, Competition>>> competitions = new HashMap<>();
    private Athlete[] athletes;
    HashMap<Station, Vector<Vector<Event>>> schedule = new HashMap<>();

    // Constructor
    public Scheduler() {
    }


    private Station getCompetitionStation(CompetitionType type) {
        return switch (type) {
            case RUNNING60, HURDLES -> Station.SPRINTLINE;
            case RUNNING200, RUNNING800, RUNNING1500, RUNNING3000 -> Station.RUNNINGCIRCLE;
            case LONGJUMPING, TRIPLEJUMPING -> Station.LONGTRIPLEJUMP;
            case HIGHJUMPING, POLEJUMPING -> Station.HIGHJUMP;
            case THROWING -> Station.SHOTTHROWING;
        };

    }

    private void addToSchedule(Vector<Event> events) {
        for (int i = 0; i < events.size(); i++) {
            Station station = getCompetitionStation(events.get(i).competitionType);
            int size = schedule.get(station).size();
            int chosenStation = i % size;
            int eventsInVector = schedule.get(station).get(chosenStation).size();

            if (eventsInVector == 0) {
                events.get(i).startTime = 0;
            } else {
                Event lastEvent = schedule.get(station).get(chosenStation).get(eventsInVector - 1);
                events.get(i).startTime = lastEvent.startTime + lastEvent.duration;
            }

            int firstAthleteIndex = events.get(i).participants.get(0);

            events.get(i).ageGroup = getAgeGroup(athletes[firstAthleteIndex].getAge());
            events.get(i).station = station;
            events.get(i).stationIndex = chosenStation;

            if (athletes[firstAthleteIndex].getAge() < 15) {
                events.get(i).sexCategory = SexCategory.Both;
            } else {
                events.get(i).sexCategory = athletes[firstAthleteIndex].getSex();
            }

            schedule.get(station).get(chosenStation).add(events.get(i));
        }
    }

    // Generate schedule
    public void generateSchedule(String filePath) {
        initializeAthletes(filePath);
        fillCompetitions();
        for (HashMap<SexCategory, HashMap<CompetitionType, Competition>> age : competitions.values()) {
            for (HashMap<CompetitionType, Competition> sex : age.values()) {
                for (Competition type : sex.values()) {
                    type.calculateGroups();
                }
            }
        }

        for (Station station : Station.values())
            schedule.put(station, new Vector<>());
        schedule.get(Station.RUNNINGCIRCLE).add(new Vector<>());
        schedule.get(Station.SPRINTLINE).add(new Vector<>());
        schedule.get(Station.LONGTRIPLEJUMP).add(new Vector<>());
        schedule.get(Station.LONGTRIPLEJUMP).add(new Vector<>());
        schedule.get(Station.HIGHJUMP).add(new Vector<>());
        schedule.get(Station.HIGHJUMP).add(new Vector<>());
        schedule.get(Station.POLEVAULT).add(new Vector<>());
        schedule.get(Station.SHOTTHROWING).add(new Vector<>());
        schedule.get(Station.SHOTTHROWING).add(new Vector<>());
        schedule.get(Station.AWARDCEREMONYAREA).add(new Vector<>());

        for (HashMap<SexCategory, HashMap<CompetitionType, Competition>> age : competitions.values()) {
            for (HashMap<CompetitionType, Competition> sex : age.values()) {
                for (Competition type : sex.values()) {
                    while (true) {
                        Vector<Event> events = type.getNext();
                        if (events.size() == 0) break;
                        addToSchedule(events);
                    }
                }
            }
        }

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

    // Recursive function to add a competitor to a competition
    // If a key and value does not exist, it is created and the
    // function is called once again until we can add "athleteID"
    // to the competition vector
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
