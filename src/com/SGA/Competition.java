package com.SGA;

import java.util.Vector;

abstract public class Competition {
    public Vector<Integer> athleteIds = new Vector<>();
    private Vector<Vector<Vector<Integer>>> qualifierGroups;
    private int getterIndex = 0;
    private int duration = 0;

    abstract public void calculateGroups();

    public Vector<Event> getNext() {
        Vector<Event> events = new Vector<>();
        for (int i = 0; i < qualifierGroups.get(getterIndex).size(); i++) {
            Event tmpEvent = new Event();
            tmpEvent.participants.addAll(qualifierGroups.get(getterIndex).get(i));
            tmpEvent.duration = duration;
            tmpEvent.priorityIndex = getterIndex;
            // The rest the information about the event will be assigned by the scheduler
            events.add(tmpEvent);
        }
        getterIndex++;
        return events;
    }
}
