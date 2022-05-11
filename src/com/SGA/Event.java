package com.SGA;

import java.util.Vector;

public class Event {
    public CompetitionType competitionType;
    public Vector<Integer> participants;
    public int startTime;
    public int duration;
    // Priority index is used for sorting events, we give finals the highest value, which will inform the scheduler
    // that this event needs to happen after all similar events with lower priorityIndex.
    // Index 0 is assigned to the first chunk of events returned by the getNext function of the Competition class.
    public int priorityIndex;
}
