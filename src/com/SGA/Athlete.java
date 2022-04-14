package com.SGA;

/* test code by Rodriguez ,
 * ville bara veta hur kan man på bästa sätet skapar lämpliga klasser
 * för projectet
 */

import java.util.ArrayList;


public class Athlete {
    private String club;
    private String firstName;
    private String surName;
    private Sex sex;
    private int age;
    private ArrayList<Competition> competitionArray; // each athlete should have a list of competitions he participate in
    private int numOfCompetitions; // number of competitions a athlete "A" is participating

    // === constructors ===
    protected Athlete(){
        competitionArray = new ArrayList<Competition>(); // creating the array
        this.numOfCompetitions = 0;
    }

    protected Athlete(String club, String firstName, String surName, Sex sex, int age) {
        competitionArray = new ArrayList<Competition>(); // creating the array
        this.club = club;
        this.firstName = firstName;
        this.surName = surName;
        this.sex = sex;
        this.age = age;
        this.numOfCompetitions = 0;
    }


    // === getters ===
    public String getClub() {
        return club;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurName() {
        return surName;
    }

    public Sex getSex() {
        return sex;
    }

    public int getAge() {
        return age;
    }

    public ArrayList<Competition> getCompetitions() {
        return competitionArray;
    }

    // === setters ===
    public void setCompetitions(Competition competition, ArrayList<Competition> competitionArray) {
        this.competitionArray.add( competition );
        this.numOfCompetitions += 1;
        competition.calculateNumOfParticipate(competitionArray);
    }

    public int getNumOfCompetitions() {
        return numOfCompetitions;
    }

    public void setNumOfCompetitions(int numOfCompetitions) {
        this.numOfCompetitions = numOfCompetitions;

    }

    // displaying athlete information
    public void diaplay(){
        System.out.println("Clup               : " + this.club);
        System.out.println("Name               : " + this.firstName + " " + this.surName);
        System.out.printf("Sex                : "); printSex(this.sex);
        System.out.println("Num of competitions: " + this.getNumOfCompetitions());
        System.out.println("====  Competitions   === ");
        for (Competition i : competitionArray){
            i.displayCompetition();
        }
    }

    private void printSex(Sex sex){
        switch (sex){
            case F -> System.out.printf("Female\n");
            case M -> System.out.printf("Male\n");
        }
    }



}
