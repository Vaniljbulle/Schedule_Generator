package com.SGA;

import java.util.*;

public class Scheduler {
    HashMap<AgeGroup, HashMap<SexCategory, HashMap<CompetitionType, Competition>>> competitions = new HashMap<>();
    private Athlete[] athletes;
    HashMap<Station, Vector<Vector<Event>>> schedule = new HashMap<>();
    Vector<Event> allEvents = new Vector<>();
    private int breakTime = 5;
    private int lunchTime = 30;
    private int lunchStartTime = 720; // 12:00
    private int startTimeOfTheDay = 420; // 7:00
    private int endTimeOfTheDay = 1020; // 17:00
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
                    if (doesCollide(event, newEvent) ||
                            (event.priorityIndex < newEvent.priorityIndex && event.startTime > newEvent.endTime)) {
                        overlappingOwnStation(newEvent, event);
                    }
                } else {
                    if (event.priorityIndex < newEvent.priorityIndex && event.startTime > newEvent.endTime) {
                        overlappingOwnStation(newEvent, event);
                    }
                }
            }
        }
    }

    private void moveIfOverlap(Event newEvent) {
        for (Event event : allEvents) {
            if (getCompetitionStation(event.competitionType) == getCompetitionStation(newEvent.competitionType)) {
                if (event.stationIndex == newEvent.stationIndex) {
                    if (doesCollide(event, newEvent) ||
                            (event.priorityIndex < newEvent.priorityIndex)) {
                        overlappingOwnStation(newEvent, event);
                    }
                } else {
                    if (event.priorityIndex < newEvent.priorityIndex) {
                        overlappingGeneral(newEvent, event);
                    }
                }
            } else {
                if (doesCollide(event, newEvent)) {
                    overlappingGeneral(newEvent, event);
                }
            }
        }
    }

    // Finds an available position in the schedule for a new event
    private void findAvailablePosition(Event newEvent) {
        //moveIfOverlapOwnStation(newEvent);
        //moveIfOverlapGeneral(newEvent);
        moveIfOverlap(newEvent);
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
            allEvents.add(events.get(i));
            sortEvents(allEvents);
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
    public void generateSchedule(String filePath, String filePathOut) {
        initializeAthletes(filePath);
        System.out.println("Athletes initialized after " + getPerformance() + " ms");
        fillCompetitions();
        System.out.println("Competitions filled after " + getPerformance() + " ms");
        initialiseCompetitions();
        System.out.println("Competitions initialized after " + getPerformance() + " ms");
        initialiseStations();
        System.out.println("Stations initialized after " + getPerformance() + " ms");
        addAllEvents();
        System.out.println("Events added after " + getPerformance() + " ms");
        String schedule = generateCSV();
        System.out.println("CSV generated after " + getPerformance() + " ms");
        saveSchedule(filePathOut, schedule);
        System.out.println("CSV saved after " + getPerformance() + " ms");

        System.out.println("\nThe schedule has been successfully generated into the given file");
    }

    long timeMilli = new Date().getTime();

    private long getPerformance() {
        Date date = new Date();
        long performance = date.getTime() - timeMilli;
        timeMilli = date.getTime();
        return performance;
    }

    private void saveSchedule(String filePath, String schedule) {
        FileHandler f = new FileHandler();
        f.writeFileToPath(filePath, schedule);
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
        int[] time = {0, 0, 0};

        while (timeInMinutes > endTimeOfTheDay) {
            time[0]++;
            timeInMinutes -= (endTimeOfTheDay - startTimeOfTheDay);
        }
        time[1] = timeInMinutes / 60;
        time[2] = timeInMinutes % 60;

        //System.out.println("Day = " + time[0] + " Time " + time[1] + ":" +time[2]);


        return time;
    }
    /*
    //Translate Time From Minutes To Houres && minutes
    public int[] translateTime(int timeInMinutes) {
        int[] time = {0, 0, 0};

//        while (timeInMinutes > endTimeOfTheDay) {
//            time[0]++;
//            timeInMinutes -= (endTimeOfTheDay - startTimeOfTheDay);
//        }
        time[0] = timeInMinutes / oneDayInMinutes;
        time[1] = (timeInMinutes / 60) % 24;
        time[2] = timeInMinutes % 60;

        //System.out.println("Day = " + time[0] + " Time " + time[1] + ":" +time[2]);


        return time;
    }


     */

    /*
     *  Cell style for the table
     *  DZ-XX:XX-YY:YY-()
     *  Z = Day
     *  X = Start time
     *  Y = End time
     *  () = Participants ID (Multiple participants delimited by ';')
     *
     *  Example: D1-13:37-13:38-(57;108;)
     *  D1 = Day 1
     *  13:37 = Start time
     *  13:38 = End time
     *  57 = Participant 1
     *  108 = Participant 2
     */
    private String generateCSV() {
        // Sort competitions
        for (Station station : Arrays.asList(Station.RUNNINGCIRCLE, Station.LONGTRIPLEJUMP, Station.HIGHJUMP, Station.SHOTTHROWING, Station.POLEVAULT, Station.SPRINTLINE, Station.AWARDCEREMONYAREA)) {
            sortEvents(schedule.get(station).get(0));
        }

        StringBuilder csv = new StringBuilder();

        // Header of the csv file
        csv.append("RunningCircle,LongTripleJump,HighJump,ShotThrow,PoleVault,SprintLine,Awards\n");

        // Get the largest size of the events
        int maxSize = Math.max(schedule.get(Station.RUNNINGCIRCLE).get(0).size(),
                Math.max(schedule.get(Station.LONGTRIPLEJUMP).get(0).size(),
                        Math.max(schedule.get(Station.HIGHJUMP).get(0).size(),
                                Math.max(schedule.get(Station.SHOTTHROWING).get(0).size(),
                                        Math.max(schedule.get(Station.POLEVAULT).get(0).size(),
                                                Math.max(schedule.get(Station.SPRINTLINE).get(0).size(),
                                                        schedule.get(Station.AWARDCEREMONYAREA).get(0).size()))))));

        // Build the csv file
        for (int i = 0; i < maxSize; i++) {
            String row;
            row = getCell(schedule.get(Station.RUNNINGCIRCLE).get(0), i);
            row += "," + getCell(schedule.get(Station.LONGTRIPLEJUMP).get(0), i);
            row += "," + getCell(schedule.get(Station.HIGHJUMP).get(0), i);
            row += "," + getCell(schedule.get(Station.SHOTTHROWING).get(0), i);
            row += "," + getCell(schedule.get(Station.POLEVAULT).get(0), i);
            row += "," + getCell(schedule.get(Station.SPRINTLINE).get(0), i);
            row += "," + getCell(schedule.get(Station.AWARDCEREMONYAREA).get(0), i);

            csv.append(row).append("\n");
        }

        //System.out.println(csv.toString());
        return csv.toString();
    }

    // Delay events start time and end time
    private void delayEvents(Vector<Event> events, int delay) {
        for (Event event : events) {
            event.startTime = event.startTime + delay;
            event.endTime = event.endTime + delay;
        }
    }

    // Delay all events in the schedule
    private void delaySchedule(int delay) {
        for (Station station : Arrays.asList(Station.RUNNINGCIRCLE, Station.LONGTRIPLEJUMP, Station.HIGHJUMP, Station.SHOTTHROWING, Station.POLEVAULT, Station.SPRINTLINE, Station.AWARDCEREMONYAREA)) {
            delayEvents(schedule.get(station).get(0), delay);
        }
    }

    private String getCell(Vector<Event> event, int i) {
        StringBuilder row = new StringBuilder();
        int[] timeStart, timeEnd;
        try {
            timeStart = translateTime(event.get(i).startTime);
            timeEnd = translateTime(event.get(i).endTime);

            // If time is between 12 && 13, delay schedule by an hour
            if (timeEnd[1] == 12){
                delaySchedule(60 + 60 - timeStart[2]);
                timeStart = translateTime(event.get(i).startTime);
                timeEnd = translateTime(event.get(i).endTime);
            }

            // If end time is before start time, delay schedule by difference between end of day and last start time
            // e.g 16:55-07:05 -> diff = 6
            if (timeEnd[1] < timeStart[1]) {
                delaySchedule(61-timeStart[2]);
                timeStart = translateTime(event.get(i).startTime);
                timeEnd = translateTime(event.get(i).endTime);
            }

            row.append("D").append(timeStart[0]).append("-").append(String.format("%02d", timeStart[1])).append(":").append(String.format("%02d", timeStart[2])).append("-");
            row.append(String.format("%02d", timeEnd[1])).append(":").append(String.format("%02d", timeEnd[2])).append("-");
        /*
        int[] time;
        try {
            int top = event.get(i).startTime + oneDayInMinutes - endTimeOfTheDay;
            int nights = top / oneDayInMinutes;
            int nightLength = oneDayInMinutes - endTimeOfTheDay + startTimeOfTheDay;
            event.get(i).startTime += nights * nightLength;
            event.get(i).endTime += nights * nightLength;
            // Start time
            time = translateTime(event.get(i).startTime);
            row.append("D").append(time[0]).append("-").append(String.format("%02d", time[1])).append(":").append(String.format("%02d", time[2])).append("-");

            // End time
            time = translateTime(event.get(i).endTime);
            row.append(String.format("%02d", time[1])).append(":").append(String.format("%02d", time[2])).append("-");
*/

            // Participants
            row.append("(");
            for (int j = 0; j < event.get(i).participants.size(); j++) {
                row.append(event.get(i).participants.get(j)).append(";");
            }
            row.append(")");
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        return row.toString();
    }

    private void sortEvents(Vector<Event> event) {
        event.sort((o1, o2) -> {
            if (o1.startTime > o2.startTime) return 1;
            else if (o1.startTime < o2.startTime) return -1;
            return 0;
        });
    }

    /*
    // Insertion sort
    private void sortEvents(Vector<Event> events) {
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
*/

    /*
    // Quick sort
    private void sortEvents(Vector<Event> events) {
        quickSort(events, 0, events.size() - 1);
    }

    private void quickSort(Vector<Event> events, int low, int high) {
        int i = low, j = high;
        Event pivot = events.get(low + (high - low) / 2);

        while (i <= j) {
            while (events.get(i).startTime < pivot.startTime) {
                i++;
            }

            while (events.get(j).startTime > pivot.startTime) {
                j--;
            }

            if (i <= j) {
                Event temp = events.get(i);
                events.set(i, events.get(j));
                events.set(j, temp);
                i++;
                j--;
            }
        }

        if (low < j)
            quickSort(events, low, j);

        if (i < high)
            quickSort(events, i, high);
    }
    */

    /*
    // Merge sort
    private void sortEvents(Vector<Event> events) {
        mergeSort(events, 0, events.size() - 1);
    }

    private void mergeSort(Vector<Event> events, int low, int high) {
        if (low < high) {
            int mid = (low + high) / 2;
            mergeSort(events, low, mid);
            mergeSort(events, mid + 1, high);
            merge(events, low, mid, high);
        }
    }

    private void merge(Vector<Event> events, int low, int mid, int high) {
        Vector<Event> temp = new Vector<>();
        int i = low, j = mid + 1;
        while (i <= mid && j <= high) {
            if (events.get(i).startTime < events.get(j).startTime) {
                temp.add(events.get(i));
                i++;
            }
            else {
                temp.add(events.get(j));
                j++;
            }
        }
        while (i <= mid) {
            temp.add(events.get(i));
            i++;
        }
        while (j <= high) {
            temp.add(events.get(j));
            j++;
        }
        for (int k = low; k <= high; k++) {
            events.set(k, temp.get(k - low));
        }
    }
    */
}


