package com.SGA;

import java.util.Vector;

abstract public class Competition {
    public Vector<Integer> athleteIds = new Vector<>();
    protected Vector<Vector<Vector<Integer>>> qualifierGroups = new Vector<>();
    protected int getterIndex = 0;
    protected int duration = 10;
    protected CompetitionType competitionType;

    abstract public void calculateGroups();

    public Vector<Event> getNext() {
        if (getterIndex > qualifierGroups.size() - 1) return new Vector<>();
        //calculateDuration();
        Vector<Event> events = new Vector<>();
        for (int i = 0; i < qualifierGroups.get(getterIndex).size(); i++) {
            Event tmpEvent = new Event();
            tmpEvent.participants.addAll(qualifierGroups.get(getterIndex).get(i));
            tmpEvent.duration = duration;
            tmpEvent.priorityIndex = getterIndex;
            tmpEvent.competitionType = competitionType;
            // The rest the information about the event will be assigned by the scheduler
            events.add(tmpEvent);
        }
        getterIndex++;
        return events;
    }

    public void calculateDuration(Athlete[] athletes) {
        double highest = 0;
        double tmp = 0;
        // get the highest value
        for (int i = 0; i < athleteIds.size(); i++) {
            if (athletes[i].getCompetitionResults().containsKey(competitionType)) {
                tmp = athletes[i].getCompetitionResults().get(competitionType);
            }
            if (tmp > highest) // checks if we get higher value
                highest = tmp;
        }

        duration = (int) highest;
        duration = (int) Math.ceil(1.1 * duration / 60.0);
    }

}
