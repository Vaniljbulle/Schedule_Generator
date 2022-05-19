package com.SGA;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

public class Scheduler {
    HashMap<AgeGroup, HashMap<SexCategory, HashMap<CompetitionType, Competition>>> competitions = new HashMap<>();
    private Athlete[] athletes;
    HashMap<Station, Vector<Vector<Event>>> schedule = new HashMap<>();

    private int breakTime = 5;
    private int startTimeOfTheDay = 420; // 7:00
    private int endTimeOfTheDay = 1260; // 21:00
    private int oneDayInMinutes = 1440; // 24:00

    // Constructor
    public Scheduler() {
    }

    // Returns the station that fits the CompetitionType
    private Station getCompetitionStation(CompetitionType type) {
        return switch (type) {
            case RUNNING60, HURDLES -> Station.SPRINTLINE;
            case RUNNING200, RUNNING800, RUNNING1500, RUNNING3000 -> Station.RUNNINGCIRCLE;
            case LONGJUMPING, TRIPLEJUMPING -> Station.LONGTRIPLEJUMP;
            case HIGHJUMPING -> Station.HIGHJUMP;
            case POLEJUMPING -> Station.POLEVAULT;
            case THROWING -> Station.SHOTTHROWING;
        };

    }

    // Returns whether the event times collide
    private boolean doesCollide(Event event1, Event event2) {
        return event1.day == event2.day && event1.endTime > event2.startTime && event1.startTime < event2.endTime;
    }

    // Returns whether the events have any of the same athletes
    private boolean hasOverlappingAthletes(Event event1, Event event2) {
        for (int a1 : event1.participants) {
            for (int a2 : event2.participants) {
                if (a1 == a2) return true;
            }
        }
        return false;
    }

    // if newEvent and otherEvent share any participants:
    // Moves newEvent's timeslot to breakTime minutes after otherEvent's endTime
    private void overlappingGeneral(Event newEvent, Event otherEvent) {
        if (hasOverlappingAthletes(otherEvent, newEvent)) {
            newEvent.startTime = otherEvent.endTime + breakTime;
            newEvent.endTime = newEvent.startTime + newEvent.duration;
        }
    }

    // Checking if newEvent collide with any other event, then adjust timeslot accordingly
    private void moveIfOverlapGeneral(Event newEvent) {
        for (Vector<Vector<Event>> stations : schedule.values()) {
            for (Vector<Event> events : stations) {
                for (Event event : events) {
                    if (doesCollide(event, newEvent)) {
                        overlappingGeneral(newEvent, event);
                    }
                }
            }
        }
    }

    // if newEvent and otherEvent share any participants:
    // Moves newEvent's timeslot to breakTime minutes after otherEvent's endTime
    // else: Moves newEvent's timeslot to right after otherEvent's endTime
    private void overlappingOwnStation(Event newEvent, Event otherEvent) {
        if (hasOverlappingAthletes(otherEvent, newEvent))
            newEvent.startTime = otherEvent.endTime + breakTime;
        else
            newEvent.startTime = otherEvent.endTime;
        newEvent.endTime = newEvent.startTime + newEvent.duration;
    }

    // Checking if newEvent collide with any other event in its station, then adjust timeslot accordingly
    private void moveIfOverlapOwnStation(Event newEvent) {
        for (Vector<Event> events : schedule.get(newEvent.station)) {
            for (Event event : events) {
                if (event.stationIndex == newEvent.stationIndex) {
                    if (doesCollide(event, newEvent) || event.priorityIndex < newEvent.priorityIndex) {
                        overlappingOwnStation(newEvent, event);
                    }
                } else {
                    if (event.priorityIndex < newEvent.priorityIndex) {
                        overlappingOwnStation(newEvent, event);
                    }
                }
            }
        }
    }

    // Finds an available position in the schedule for a new event
    private void findAvailablePosition(Event newEvent) {
        moveIfOverlapOwnStation(newEvent);
        moveIfOverlapGeneral(newEvent);
    }

    // Adds all events the vector to the schedule
    private void addToSchedule(Vector<Event> events) {
        for (int i = 0; i < events.size(); i++) {
            Station station = getCompetitionStation(events.get(i).competitionType);
            int size = schedule.get(station).size();
            int chosenStation = i % size;

            events.get(i).startTime = startTimeOfTheDay;
            events.get(i).endTime = events.get(i).startTime + events.get(i).duration;

            int firstAthleteIndex = events.get(i).participants.get(0);

            events.get(i).ageGroup = getAgeGroup(athletes[firstAthleteIndex].getAge());
            events.get(i).station = station;
            events.get(i).stationIndex = chosenStation;

            if (athletes[firstAthleteIndex].getAge() < 15) {
                events.get(i).sexCategory = SexCategory.Both;
            } else {
                events.get(i).sexCategory = athletes[firstAthleteIndex].getSex();
            }

            findAvailablePosition(events.get(i));

            schedule.get(station).get(chosenStation).add(events.get(i));
        }
    }

    // Calculates groups and duration for all competitions
    private void initialiseCompetitions() {
        for (HashMap<SexCategory, HashMap<CompetitionType, Competition>> age : competitions.values()) {
            for (HashMap<CompetitionType, Competition> sex : age.values()) {
                for (Competition type : sex.values()) {
                    type.calculateGroups(athletes);
                    type.calculateDuration(athletes);
                }
            }
        }
    }

    // Adds all stations to the schedule
    private void initialiseStations() {
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
    }

    // Adds all events to the schedule
    private void addAllEvents() {
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

    // Generate schedule
    public void generateSchedule(String filePath) {
        initializeAthletes(filePath);
        fillCompetitions();
        initialiseCompetitions();
        initialiseStations();
        addAllEvents();

        generateCSV();
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

    //Translate Time From Minutes To Houres && minutes
    public int[] translateTime(int timeInMinutes) {
        int [] time = {0, 0, 0};

        while(timeInMinutes > 1260) {
            time[0]++;
            timeInMinutes -= 840;
        }
        time[1] = timeInMinutes / 60;
        time[2] = timeInMinutes % 60;

        //System.out.println("Day = " + time[0] + " Time " + time[1] + ":" +time[2]);


        return time;
    }


    private void generateCSV() {
        StringBuilder csv = new StringBuilder();

        Vector<Event> runningCircle = new Vector<>(schedule.get(Station.RUNNINGCIRCLE).get(0));
        sortEvents(runningCircle);


        Vector<String> templines = new Vector<>();
        int[] lastTime = {0,0};
        for (Event event : runningCircle) {
            String temp = "";
            int[] timeStart = translateTime(event.startTime);
            if (timeStart[1] == lastTime[0] && timeStart[2] == lastTime[1]) {
                temp += "x";
            }
            int[] timeEnd = translateTime(event.endTime);
            temp += (event.competitionType.toString()) + (": Day ") + (timeStart[0]) + (" Time ") + (String.format("%02d", timeStart[1])) + (":") + (String.format("%02d", timeStart[2])) + (" - ") + (String.format("%02d", timeEnd[1])) + (":") + (String.format("%02d", timeEnd[2]));
            temp += (" ") + (event.ageGroup.toString());
            temp += (" Participants: ");
            for (int i = 0; i < event.participants.size(); i++) {
                temp += (athletes[event.participants.get(i)].getFirstName()) + (", ");
            }
            lastTime[0] = timeStart[1];
            lastTime[1] = timeStart[2];
            templines.add(temp);
        }

        // Loop through vector
        for (String templine : templines) {
            // Check if first character is "x"
            if (templine.charAt(0) == 'x') {
                // Duplicate start time
                System.err.println(templine);
            } else {
                System.out.println(templine);
            }
        }

        /*
        int[] lastTime = {0,0};
        for (Event event : runningCircle) {
            int[] timeStart = translateTime(event.startTime);
            if (timeStart[1] == lastTime[0] && timeStart[2] == lastTime[1]) {
                csv.append("x");
            }
            int[] timeEnd = translateTime(event.endTime);
            csv.append(event.competitionType.toString()).append(": Day ").append(timeStart[0]).append(" Time ").append(String.format("%02d", timeStart[1])).append(":").append(String.format("%02d", timeStart[2])).append(" - ").append(String.format("%02d", timeEnd[1])).append(":").append(String.format("%02d", timeEnd[2]));
            csv.append(" ").append(event.ageGroup.toString());
            csv.append(" Participants: ");
            for (int i = 0; i < event.participants.size(); i++) {
                csv.append(athletes[event.participants.get(i)].getFirstName()).append(", ");
            }
            csv.append("\n");
            lastTime[0] = timeStart[1];
            lastTime[1] = timeStart[2];
        }
        */


        //System.out.println(csv.toString());

    }

    // Bubble sort
    private void sortEvents(Vector<Event> events) {
        for (int i = 0; i < events.size(); i++) {
            for (int j = 0; j < events.size() - 1; j++) {
                if (events.get(j).startTime > events.get(j + 1).startTime) {
                    Event temp = events.get(j);
                    events.set(j, events.get(j + 1));
                    events.set(j + 1, temp);
                }
            }
        }
    }

    // Insertion sort
    private void sortEvents2(Vector<Event> events) {
        for (int i = 0; i < events.size(); i++) {
            Event temp = events.get(i);
            int j = i;
            while (j > 0 && events.get(j - 1).startTime > temp.startTime) {
                events.set(j, events.get(j - 1));
                j--;
            }
            events.set(j, temp);
        }
    }

}
