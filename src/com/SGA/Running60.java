package com.SGA;

import java.util.Vector;
import java.lang.Math;

public class Running60 extends Competition {
    @Override
    public void calculateGroups(Athlete[] athletes) {
        competitionType = CompetitionType.RUNNING60;
        int participantsPerEvent = 8;
        int numOfAthletes = athleteIds.size();
        int numOfGroups = (int) Math.ceil((double) numOfAthletes / participantsPerEvent);
        //System.out.println("Num Of Groups Running 60-> " + numOfGroups);

        Vector<Vector<Integer>> groups = new Vector<>();
        if (numOfAthletes > participantsPerEvent) {
            int idIndex = 0;
            for (int i = 0; i < numOfGroups; i++) {
                int participants = numOfAthletes / numOfGroups;
                numOfGroups--;
                i--;
                numOfAthletes -= participants;
                Vector<Integer> group = new Vector<>();
                for (int k = idIndex; k < idIndex + participants; k++) {
                    group.add(athleteIds.get(k));
                }
                groups.add(group);
                idIndex += participants;
            }
            qualifierGroups.add(groups);
            groups = new Vector<>();
        }
        Vector<Integer> group = new Vector<>(athleteIds);
        groups.add(group);
        qualifierGroups.add(groups);
    }

    @Override
    public Vector<Event> getNext() {
        return super.getNext();
    }
}
