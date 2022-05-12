package com.SGA;

import java.util.Vector;

public class JumpingHigh extends Competition {
    @Override
    public void calculateGroups() {
        competitionType = CompetitionType.HIGHJUMPING;
    }

    @Override
    public Vector<Event> getNext() {
        return super.getNext();
    }
}
