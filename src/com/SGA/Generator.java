package com.SGA;

import java.util.Vector;

public class Generator {

    public Vector<Vector<Competition>> competitions = new Vector<>();

    public void generate() {
        // This didn't work since in java you cannot index using enums >:(
//        competitions.setSize(AgeGroup.values().length);
//        System.out.println(competitions.size());
//        for (AgeGroup group : AgeGroup.values()) {
//            competitions.get((Integer) group).setSize(CompetitionType.values().length);
//            System.out.println(competitions.size());
//            for (CompetitionType type : CompetitionType.values()) {
//
//            }
        //competitions.add();
        //}

    }

    public void loadData() {

    }
}
