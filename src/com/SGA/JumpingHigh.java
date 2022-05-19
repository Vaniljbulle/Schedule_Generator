package com.SGA;

import java.util.Vector;

public class JumpingHigh extends Competition {
    private double highestRecord = 0;
    private Vector<Double> personalRecords = new Vector<>();
    private final Double recordMultiplier = 1.1;
    private double startHeight = 20; // High value so it will definitely override the value in calculateGroups
    private final double lowestMultiplier = 0.9;

    @Override
    public void calculateGroups(Athlete[] athletes) {
        competitionType = CompetitionType.HIGHJUMPING;

        for (int id : athleteIds) {
            double highest = athletes[id].getCompetitionResults().get(CompetitionType.HIGHJUMPING);
            personalRecords.add(highest);
            if (highest > highestRecord)
                highestRecord = highest;
            if (highest < startHeight)
                startHeight = highest;
        }

        int slightlyAboveHighest = (int) ((float) highestRecord * recordMultiplier * 100.0);
        int slightlyBelowLowest = (int) ((float) startHeight * lowestMultiplier * 100.0);
        for (int i = (int) slightlyBelowLowest; i < slightlyAboveHighest; i += 5) {
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
