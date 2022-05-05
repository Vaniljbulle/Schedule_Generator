package com.SGA;

public class Athlete {
    private String firstName;
    private String lastName;
    private int age;

    private SexCategory sex;
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

    // TODO: Finish this
    Athlete(String ath) {
        // split string with delimiter ","
        String[] athlete = ath.split(",");

        // Validate input if length is 16
        if (athlete.length == 16) {
            this.firstName = athlete[1];
            this.lastName = athlete[2];
            if (athlete[3].equals("M")) this.sex = SexCategory.Male;
            else this.sex = SexCategory.Female;
            this.age = Integer.parseInt(athlete[4]);

            int result = athlete[7].indexOf(':');

            if (result != -1) {
                this.running60 = Double.parseDouble(athlete[7].substring(result+1, athlete[7].length()));
                System.out.println(this.running60);
            }
            else if (athlete[7].equals("")) {
               // this.running60 = Double.parseDouble(athlete[5]);
                System.out.println("No result for 60m");
            }
            else {
            }

        }

    }
}
