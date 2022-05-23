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

        Generator generator = new Generator();
        schedule = generator.generate(schedule, athletes);
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

        time[0] = timeInMinutes / 1440;
        time[1] = (timeInMinutes / 60) % 24;
        time[2] = timeInMinutes % 60;

        //System.out.println("Day = " + time[0] + " Time " + time[1] + ":" +time[2]);

        return time;
    }



    /*
     *  Cell style for the table
     *  DZ-XX:XX-YY:YY-()-C-B
     *  Z = Day
     *  X = Start time
     *  Y = End time
     *  () = Participants ID (Multiple participants delimited by ';')
     *  C = Competition type
     *  B = Competition setting (0 if there is no setting to be set)
     *
     *  Example: D1-13:37-13:38-(57;108;)-RUNNING60-0
     *  D1 = Day 1
     *  13:37 = Start time
     *  13:38 = End time
     *  57 = Participant 1
     *  108 = Participant 2
     *  RUNNING60 = Sprint, 60m
     *  0 = No setting (e.g bar height)
     */
    private String generateCSV() {
        // Sort competitions
        for (Station station : Arrays.asList(Station.RUNNINGCIRCLE, Station.LONGTRIPLEJUMP, Station.HIGHJUMP, Station.SHOTTHROWING, Station.POLEVAULT, Station.SPRINTLINE, Station.AWARDCEREMONYAREA)) {
            for (int i = 0; i < schedule.get(station).size(); i++) {
                sortEvents(schedule.get(station).get(i));
            }
        }

        StringBuilder csv = new StringBuilder();

        // Loop through keys schedule
        for (Station station : Arrays.asList(Station.RUNNINGCIRCLE, Station.LONGTRIPLEJUMP, Station.HIGHJUMP, Station.SHOTTHROWING, Station.POLEVAULT, Station.SPRINTLINE, Station.AWARDCEREMONYAREA)) {
            // For  each station
            for (int i = 0; i < schedule.get(station).size(); i++) {
                System.out.println("\n" + station + " " + i);
                csv.append(station).append("(").append(i).append("),");
            }
        }
        csv.deleteCharAt(csv.length() - 1);

        // Header of the csv file
        csv.append("\n");

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
            row += "," + getCell(schedule.get(Station.LONGTRIPLEJUMP).get(1), i);
            row += "," + getCell(schedule.get(Station.HIGHJUMP).get(0), i);
            row += "," + getCell(schedule.get(Station.HIGHJUMP).get(1), i);
            row += "," + getCell(schedule.get(Station.SHOTTHROWING).get(0), i);
            row += "," + getCell(schedule.get(Station.SHOTTHROWING).get(1), i);
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

            row.append("D").append(timeStart[0]).append("-").append(String.format("%02d", timeStart[1])).append(":").append(String.format("%02d", timeStart[2])).append("-");
            row.append(String.format("%02d", timeEnd[1])).append(":").append(String.format("%02d", timeEnd[2])).append("-");

            // Participants
            row.append("(");
            for (int j = 0; j < event.get(i).participants.size(); j++) {
                row.append(event.get(i).participants.get(j)).append(";");
            }
            row.append(")-(");

            // Competition type
            row.append(event.get(i).competitionType).append(")");

            row.append("-");
            row.append(event.get(i).barHeight);
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
}


