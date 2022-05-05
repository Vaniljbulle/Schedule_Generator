package com.SGA;

public class Athlete {
    private String firstName;
    private String lastName;
    private int age;

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

    Athlete(String ath) {
        // split string with delimiter ","
        String[] name = ath.split(",");
        if (name.length == 15) {
            System.out.println("valid input");
        }
    }
}
