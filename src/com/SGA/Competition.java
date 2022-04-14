package com.SGA;

/* test code by Rodriguez ,
* ville bara veta hur kan man på bästa sätet skapar lämpliga klasser
* för projectet
*/

import java.util.ArrayList;

public class Competition {
        private String discipline; //Running, Jumping or Throwing
        private String competitionName; // Sprint, middle, long ...
        private String competitionType; // ex: 60 meter, 200 meter
        private String timePerRound; // ex: 5 mints each round
        private int numOfParticipate; // ex 30 participate sprint 200

        private String recordTime; // record for each player in each competition




    // === constructor ===
    protected Competition(){
    }
    protected Competition(String discipline, String competitionName, String competitionType ){
        this.discipline = discipline;
        this.competitionName = competitionName;
        this.competitionType = competitionType;
        this.numOfParticipate = 0;

    }

    // === Setters ===
    protected void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    protected void setRecordTime(String recordTime){
        this.recordTime = recordTime;
    }

    protected void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    protected void setCompetitionType(String competitionType) {
        this.competitionType = competitionType;
    }

    protected void setTimePerRound(String timePerRound) {
        this.timePerRound = timePerRound;
    }

    protected void setNumOfParticipate(int numOfParticipate) {
        this.numOfParticipate = numOfParticipate;
    }

    // === getters ===
    protected String getDiscipline() {
        return discipline;
    }

    protected String getRecordTime(){
        return recordTime;
    }

    protected String getCompetitionName() {
        return competitionName;
    }

    protected String getCompetitionType() {
        return competitionType;
    }

    protected String getTimePerRound() {
        return timePerRound;
    }

    protected int getNumOfParticipate() {
        return numOfParticipate;
    }

    protected void displayCompetition(){
        System.out.println("\t-> Discipline         : " + this.getDiscipline());
        System.out.println("\t-> Competition        : " + this.getCompetitionName());
        System.out.println("\t-> Competition Type   : " + this.getCompetitionType());
        System.out.println("\t-> Round Time         : " + this.getRecordTime());
        System.out.println("\t-> Num Of Participate : " + this.getNumOfParticipate());
        System.out.println("======================");

    }

    protected void calculateNumOfParticipate(ArrayList<Competition> competitionArray){
        for(Competition i : competitionArray){
            if(this.getCompetitionName().equals(i.getCompetitionName()) && this.getCompetitionType() == (i.getCompetitionType()))
                this.numOfParticipate += 1;
        }
    }

}
