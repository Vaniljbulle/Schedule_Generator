package com.SGA;

import java.util.ArrayList;

/* test code by Rodriguez ,
 * ville bara veta hur kan man på bästa sätet skapar lämpliga klasser
 * för projectet
 * What i have done
 * -> Class for competitions & its functions
 * -> Class for Athletes & its functions
 * -> Some tests in Main.
 * Förbättringar behöver visst göras vid behöv.
 * MVH Rodriguez
 */

public class Main {

    public static void main(String[] args) {

        ArrayList<Competition> competitionArray = new ArrayList<Competition>();
        // test
        Competition com1 = new Competition("Running", "Sprint", "60");
        Competition com2 = new Competition("Running", "Sprint", "60");
        Competition com3 = new Competition("Jumping", "Long", "none");
        competitionArray.add(com1);
        competitionArray.add(com2);
        competitionArray.add(com3);

        Athlete ath1 = new Athlete("Madrid", "Ahmed", "Rodriguez", Sex.M, 24);
        ath1.setCompetitions(com1, competitionArray);
        ath1.setCompetitions(com2, competitionArray);
        ath1.setCompetitions(com3, competitionArray);

        ath1.diaplay();

        System.out.println("Number of participating in  Running " + com1.getNumOfParticipate());
    }
}
