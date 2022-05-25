package com.SGA;

import java.util.Vector;

public class JumpingPole extends Competition {
    private double highestRecord = 0;
    private Vector<Double> personalRecords = new Vector<>();
    private Vector<Integer> barHeights = new Vector<>();
    private final double recordMultiplier = 1.1;
    private double startHeight = 20; // High value, so it will definitely override the value in calculateGroups
    private final double lowestMultiplier = 0.9;

    @Override
    public void calculateGroups(Athlete[] athletes) {
        competitionType = CompetitionType.POLEJUMPING;

        for (int id : athleteIds) {
            double highest = athletes[id].getCompetitionResults().get(CompetitionType.POLEJUMPING);
            personalRecords.add(highest);
            if (highest > highestRecord)
                highestRecord = highest;
            if (highest < startHeight)
                startHeight = highest;
        }

        int slightlyAboveHighest = (int) ((float) highestRecord * recordMultiplier * 100.0);
        int slightlyBelowLowest = (int) ((float) startHeight * lowestMultiplier * 100.0);
        int increment = 0;
        for (int i = slightlyBelowLowest; i < slightlyAboveHighest; i += increment) {
            increment = (int) ((double) (slightlyAboveHighest - i) * 0.5f);
            if (increment < 5) increment = 5;
            Vector<Vector<Integer>> groups = new Vector<>();
            for (int k = 0; k < athleteIds.size(); k++) {
                Vector<Integer> group = new Vector<>();
                group.add(athleteIds.get(k));
                groups.add(group);
            }
            barHeights.add(i);
            barHeights.add(i);
            qualifierGroups.add(groups);
            qualifierGroups.add(groups);
        }
    }

    @Override
    public void calculateDuration(Athlete[] athletes) {
        duration = 1;
    }

    @Override
    public Vector<Event> getNext() {
        if (getterIndex > qualifierGroups.size() - 1) return new Vector<>();
        Vector<Event> events = new Vector<>();
        for (int i = 0; i < qualifierGroups.get(getterIndex).size(); i++) {
            Event tmpEvent = new Event();
            tmpEvent.participants.addAll(qualifierGroups.get(getterIndex).get(i));
            tmpEvent.duration = duration;
            tmpEvent.priorityIndex = getterIndex;
            tmpEvent.competitionType = competitionType;
            tmpEvent.barHeight = barHeights.get(getterIndex);
            // The rest the information about the event will be assigned by the scheduler
            events.add(tmpEvent);
        }
        getterIndex++;
        return events;
    }
}
