package com.SGA;

public class Throwing extends Competition {
    @Override
    public TimeSlot getNext() {
        System.out.println("Throwing");
        return super.getNext();
    }
}
