package com.SGA;

import java.util.Vector;

public class Main {

    public static void main(String[] args) {

        Scheduler scheduler = new Scheduler();
        scheduler.generateSchedule("registration-list.csv", "out.csv");
    }

}
