package com.SGA;

import java.util.Vector;

public class JumpingHigh extends Competition {
    private double highestRecord = 0;
    private Vector<Double> personalRecords = new Vector<>();
    private final Double recordMultiplier = 1.1;
    private final int startHeight = 1;

    @Override
    public void calculateGroups(Athlete[] athletes) {
        competitionType = CompetitionType.HIGHJUMPING;

        for (int id : athleteIds) {
            double highest = athletes[id].getCompetitionResults().get(CompetitionType.HIGHJUMPING);
            personalRecords.add(highest);
            if (highest > highestRecord)
                highestRecord = highest;
        }

        int slightlyAboveHighest = (int) ((float) highestRecord * recordMultiplier * 100.0);
        for (int i = (int)startHeight; i < slightlyAboveHighest; i += 5) {
            Vector<Vector<Integer>> groups = new Vector<>();
            for (int k = 0; k < athleteIds.size(); k++) {
                int slightlyAboveRecord = (int) (personalRecords.get(k) * recordMultiplier * 100.0);
                if (slightlyAboveRecord < i) continue;
                Vector<Integer> group = new Vector<>();
                group.add(athleteIds.get(k));
                groups.add(group);
            }
            qualifierGroups.add(groups);
        }
    }

    @Override
    public void calculateDuration(Athlete[] athletes) {
        duration = 1;
    }

    @Override
    public Vector<Event> getNext() {
        return super.getNext();
    }
}
