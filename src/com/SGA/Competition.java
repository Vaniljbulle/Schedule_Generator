package com.SGA;

import java.util.Vector;

public class Competition {
    Vector<Integer> athleteIds = new Vector<>();
    Vector<Vector<Vector<Integer>>> qualifierGroups;

    public TimeSlot getNext() {

        return new TimeSlot();
    }
}
