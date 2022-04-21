package com.SGA;

import java.util.HashMap;

public class Athlete {
    public String firstName;
    public String lastName;
    public int age;
    public HashMap<CompetitionType, ParticipationInfo> participation;

    public static class ParticipationInfo {
        public Boolean isParticipating = false;
        public float recordTime = -1.0f;
    }

    Athlete(String _firstName, String _lastName, int _age) {
        firstName = _firstName;
        lastName = _lastName;
        age = _age;
    }
}
