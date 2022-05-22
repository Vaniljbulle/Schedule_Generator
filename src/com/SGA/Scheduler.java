package com.SGA;

import java.util.*;

public class Scheduler {
    private Athlete[] athletes;
    HashMap<Station, Vector<Vector<Event>>> schedule = new HashMap<>();

    // Constructor
    public Scheduler() {
    }


    // Generate schedule
    public void generateSchedule(String filePath, String filePathOut) {
        initializeAthletes(filePath);
        System.out.println("Athletes initialized after " + getPerformance() + " ms");

        Generator generator = new Generator(athletes);
        schedule = generator.generate();
        String schedule = generateCSV();
        System.out.println("CSV generated");
        saveSchedule(filePathOut, schedule);
        System.out.println("CSV saved");

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


    // Load participants data from csv file
    private String loadParticipants(String filePath) {
        FileHandler fileHandler = new FileHandler();
        fileHandler.readFileFromPath(filePath);
        return fileHandler.getFileContent();
    }
    //Translate Time From Minutes To Houres && minutes
    public int[] translateTime(int timeInMinutes) {
        int[] time = {0, 0, 0};

        // 17:00
        int endTimeOfTheDay = 1020;
        while (timeInMinutes > endTimeOfTheDay) {
            time[0]++;
            // 7:00
            int startTimeOfTheDay = 420;
            timeInMinutes -= (endTimeOfTheDay - startTimeOfTheDay);
        }
        time[1] = timeInMinutes / 60;
        time[2] = timeInMinutes % 60;

        //System.out.println("Day = " + time[0] + " Time " + time[1] + ":" +time[2]);


        return time;
    }

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
            for (int i = 0; i < schedule.get(station).size(); i++) {
                sortEvents(schedule.get(station).get(i));
            }
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
            for (int i = 0; i < schedule.get(station).size(); i++) {
                delayEvents(schedule.get(station).get(i), delay);
            }
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

    int lastTimeStart = 0;
    int lastTImeEnd = 0;
    private void checkCollision() {
        for (Station station : Arrays.asList(Station.RUNNINGCIRCLE, Station.LONGTRIPLEJUMP, Station.HIGHJUMP, Station.SHOTTHROWING, Station.POLEVAULT, Station.SPRINTLINE, Station.AWARDCEREMONYAREA)) {
            for (int i = 0; i < schedule.get(station).size(); i++) {
                for (int j = 0; j < schedule.get(station).get(i).size(); j++) {
                    Event event1 = schedule.get(station).get(i).get(j);
                    for (int k = 0; k < schedule.get(station).size(); k++) {
                        for (int h = 0; h < schedule.get(station).get(k).size(); h++) {
                            Event event2 = schedule.get(station).get(k).get(h);
                            if (event1 == event2) continue;
                            if (doesCollide(event1, event2) && hasOverlappingAthletes(event1, event2)) {
                                System.out.println("Collision at: " + station + " " + i + " " + (j+2) + " Other event: " + station + " " + k + " " + (h+2));
                                System.out.println("Participants: " + event1.participants.toString() + " Other event: " + event2.participants.toString());
                                System.out.println("Times: " + event1.startTime + " - " + event1.endTime + " Other event: " + event2.startTime + " - " + event2.endTime);
                            }
                        }
                    }
//                    if (schedule.get(station).get(i).get(j).startTime == lastTimeStart) {
//                        System.out.println("Collision at " + station + " " + i + " " + j);
//                    }
//                    lastTimeStart = schedule.get(station).get(i).get(j).startTime;
//
//                    if (schedule.get(station).get(i).get(j).endTime == lastTImeEnd) {
//                        System.out.println("Collision at " + station + " " + i + " " + j);
//                    }
//                    lastTImeEnd = schedule.get(station).get(i).get(j).endTime;
                }
            }

        }
    }

    private void sortEvents(Vector<Event> event) {
        event.sort((o1, o2) -> {
            if (o1.startTime > o2.startTime) return 1;
            else if (o1.startTime < o2.startTime) return -1;
            return 0;
        });
    }
}


