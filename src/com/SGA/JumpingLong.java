package com.SGA;

import java.util.Vector;

public class JumpingLong extends Competition{
    @Override
    public void calculateGroups() {
        competitionType = CompetitionType.LONGJUMPING;
    }
    @Override
    public Vector<Event> getNext() {
        return super.getNext();
    }
}
