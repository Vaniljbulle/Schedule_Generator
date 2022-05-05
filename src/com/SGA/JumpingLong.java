package com.SGA;

public class JumpingLong extends Competition{
    @Override
    public TimeSlot getNext() {
        System.out.println("Jumping Long");
        return super.getNext();
    }
}
