package com.SGA;

import java.util.Vector;

public class Event {
    public CompetitionType competitionType;
    public Station station;
    public int stationIndex;
    public Vector<Integer> participants = new Vector<>();
    public int day;
    public int startTime;
    public int endTime;
    public int duration;
    public int barHeight = 0;
    // Priority index is used for sorting events, we give finals the highest value, which will inform the scheduler
    // that this event needs to happen after all similar events with lower priorityIndex.
    // Index 0 is assigned to the first chunk of events returned by the getNext function of the Competition class.
    public int priorityIndex;
    public AgeGroup ageGroup;
    public SexCategory sexCategory;

    public String toString() {
        String s = "";
        for (int i = 0; i < participants.size(); i++) {
            s += participants.get(i) + " ";
        }
        return s;
    }

    public void printCompetitionsInformation(){
        System.out.println("Competitions Type : " + this.competitionType);
        System.out.println("Station           : " + this.station);
        System.out.println("Station index     : " + this.stationIndex);
        System.out.println("start time        : " + this.startTime);
        System.out.println("End Time          : " + this.endTime);
        System.out.println("Duration          : " + this.duration);
        System.out.println("AgeGroup          : " + this.ageGroup);
        System.out.println("Sex               : " + this.sexCategory);
        System.out.println("Nr Of Athletes    : " + this.participants.size());
    }


}
