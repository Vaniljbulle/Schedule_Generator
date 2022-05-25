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
}
