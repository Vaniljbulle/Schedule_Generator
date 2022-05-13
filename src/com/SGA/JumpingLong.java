package com.SGA;

import java.util.Vector;

public class JumpingLong extends Competition{
    @Override
    public void calculateGroups() {
        competitionType = CompetitionType.LONGJUMPING;
        Vector<Vector<Integer>> groups = new Vector<>();
        for (int id : athleteIds) {
            Vector<Integer> group = new Vector<>();
            group.add(id);
            groups.add(group);
        }
        qualifierGroups.add(groups);
        qualifierGroups.add(groups);
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
