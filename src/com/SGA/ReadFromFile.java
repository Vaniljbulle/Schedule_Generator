package com.SGA;

// by Rodriguez
// Class to read & extract data from the csv file

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/* To be done (by Rodriguez next time)
* 1. declare variables to extract data from each line of the file
* 2. create objects of athlete & competitions automatic and save them in array
* 3. The function ReadFile should return a list of athlete objects
* 4. Write a function to return a list of all competitions.
* */
public class ReadFromFile {

    static protected ArrayList<Athlete> athletesMemory = new ArrayList<Athlete>();

    static public void readFile(String fileName) {
        String buffer;


        // checks if fileName has extention ".cvs", if not add it
        if (!fileName.substring(fileName.length() - 4, fileName.length()).equals(".csv")) {
            fileName += ".csv";
            System.out.println("I added .csv ");
        }
        System.out.println("File Name After = " + fileName);

        // Start Reading From File
        try {
            // open the file
            File myFile = new File(fileName);
            Scanner inputFromFile = new Scanner(myFile);
            while (inputFromFile.hasNextLine()) {
                buffer = inputFromFile.nextLine(); // Read a line
                extractData(buffer); // extract the readed line in order to take variables from each line.
            }
        } catch (FileNotFoundException error) {
            System.out.println("The File Is Not Exist");
        }


    }

    // delete the comma "," from each line from the file & extract variables for each atlete & competition.
    static private void extractData(String buffer) {
        // club, fName, sName, Sex, Age, competitions
        String[] splittedBuffer = new String[50];
        System.out.println("Extracted Line = ");
        splittedBuffer = buffer.split(",");
        for (String i : splittedBuffer) {
            System.out.println("-> " + i);
        }

        String clup, firstName, lastName;
        Sex sex;
        int age;
        clup = assignValue(splittedBuffer[0]); // checks the value is empty or not
        firstName = assignValue(splittedBuffer[1]);
        lastName = assignValue(splittedBuffer[2]);
        switch (splittedBuffer[3]){
            case "F" -> sex = Sex.F;
            case "f" -> sex = Sex.F;
            case "M" -> sex = Sex.M;
            case "m" -> sex = Sex.M;
            default -> sex = null;
        }
        age = assignInt(splittedBuffer[4]);

        System.out.println("clup         : " + clup);
        System.out.println("first Name   : " + firstName);
        System.out.println("last Name    : " + lastName);
        System.out.println("Sex          : " + sex);
        System.out.println("Age          : " + age);

        // create new athlete & add it to the athlete memory
        Athlete obj = new Athlete(clup, firstName, lastName, sex, age);
        athletesMemory.add(obj);

        /// needed to be done: 1. extract competitions & create objects & add them to competitions memory



    }

    static private String assignValue(String elemtnFromBuffer){
        if( ! elemtnFromBuffer.equals("")){
            return elemtnFromBuffer;
        }
        else
            return null;
    }

    static private int assignInt(String elemtnFromBuffer){
        if( ! elemtnFromBuffer.equals("")){
            return Integer.parseInt( elemtnFromBuffer );
        }
        else
            return -555; // means that the fild is empty
    }

    static private float assignfloat(String elemtnFromBuffer){
        if( ! elemtnFromBuffer.equals("")){
            return Float.parseFloat( elemtnFromBuffer );
        }
        else
            return -555; // means that the fild is empty
    }
}