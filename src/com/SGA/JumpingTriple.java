package com.SGA;

import java.util.Vector;

public class JumpingTriple extends Competition {
    @Override
    public void calculateGroups() {
        competitionType = CompetitionType.TRIPLEJUMPING;
    }
    @Override
    public Vector<Event> getNext() {
        return super.getNext();
    }
}
