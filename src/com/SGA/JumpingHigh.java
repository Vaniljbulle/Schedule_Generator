package com.SGA;

import java.util.Vector;

public class JumpingHigh extends Competition {
    @Override
    public void calculateGroups() {
        competitionType = CompetitionType.HIGHJUMPING;
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
