package com.SGA;

import java.util.Vector;

public class JumpingPole extends Competition{
    @Override
    public void calculateGroups() {
        competitionType = CompetitionType.POLEJUMPING;
    }
    @Override
    public Vector<Event> getNext() {
        return super.getNext();
    }
}
