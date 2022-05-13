package com.SGA;

import java.util.Vector;

public class JumpingPole extends Competition{
    @Override
    public void calculateGroups() {
        competitionType = CompetitionType.POLEJUMPING;
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
