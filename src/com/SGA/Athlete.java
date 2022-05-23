package com.SGA;

import java.util.HashMap;

public class Athlete {
    private String firstName;
    private String lastName;
    private int age;

    public SexCategory getSex() {
        return sex;
    }

    private SexCategory sex;
    /*
    private double running60;
    private double running200;
    private double running800;
    private double running1500;
    private double running3000;
    private double runningHurdles60;
    private double jumpingLong;
    private double jumpingTriple;
    private double jumpingHigh;
    private double jumpingPole;
    private double throwingShot;
    */
    private HashMap<CompetitionType, Double> competitionResults;


    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // Return hashmap
    public HashMap<CompetitionType, Double> getCompetitionResults() {
        return competitionResults;
    }

    // TODO: Add more checks to see if input is valid
    Athlete(String ath) {
        // Split string with delimiter ","
        String[] athlete = ath.split(",");

        // Validate input if length is 16
        if (athlete.length == 16) {
            this.firstName = athlete[1];
            this.lastName = athlete[2];
            if (athlete[3].equals("M")) this.sex = SexCategory.Male;
            else this.sex = SexCategory.Female;
            this.age = Integer.parseInt(athlete[4]);

            // Setup hashmap
            competitionResults = new HashMap<>();

            competitionResults.put(CompetitionType.RUNNING60, convertTime(athlete[5]));
            competitionResults.put(CompetitionType.RUNNING200, convertTime(athlete[6]));
            competitionResults.put(CompetitionType.RUNNING800, convertTime(athlete[7]));
            competitionResults.put(CompetitionType.RUNNING1500, convertTime(athlete[8]));
            competitionResults.put(CompetitionType.RUNNING3000, convertTime(athlete[9]));
            competitionResults.put(CompetitionType.HURDLES, convertTime(athlete[10]));
            competitionResults.put(CompetitionType.LONGJUMPING, convertTime(athlete[11]));
            competitionResults.put(CompetitionType.TRIPLEJUMPING, convertTime(athlete[12]));
            competitionResults.put(CompetitionType.HIGHJUMPING, convertTime(athlete[13]));
            competitionResults.put(CompetitionType.POLEJUMPING, convertTime(athlete[14]));
            competitionResults.put(CompetitionType.THROWING, convertTime(athlete[15]));

            //System.out.println(competitionResults.get(CompetitionType.RUNNING60));
            //System.out.println(competitionResults.get(CompetitionType.THROWING));
        }

    }

    // Converts input string time to seconds.milliseconds
    // e.g "00:01.000" to 1
    // e.g "01:01.001" to 61.001
    // returns -1 if input is invalid or non-existent
    private double convertTime(String time) {
        double secondsResult;

        // Get ":" index and process string if it does exist
        int result = time.indexOf(':');
        if (result != -1) {
            /*
             * e.g 03:05.123 -> 3*60 + 5.123
             * First half of the string is the minutes
             * Second half of the string is the seconds.milliseconds
             */
            secondsResult = parseDouble(time.substring(result + 1));
            secondsResult += 60 * parseDouble(time.substring(0, result));
        }
        // Else just parse time instantly, if time doesnt exist, return -1
        else {
            secondsResult = parseDouble(time);
        }

        return secondsResult;
    }

    // Parse a string to double
    // Returns -1 if input is invalid or non-existent
    private double parseDouble(String str) {
        double result;
        try {
            result = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            //System.err.println("Invalid input: " + e.getMessage() + ", returning -1");
            //System.err.println("Input: " + str);
            result = -1;
        }
        return result;
    }
}
