package com.SGA;

// by Rodriguez
// Class to read & extract data from the csv file

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/* To be done (by Rodriguez next time)
* 1. declare variables to extract data from each line of the file
* 2. create objects of athlete & competitions automatic and save them in array
* 3. The function ReadFile should return a list of athlete objects
* 4. Write a function to return a list of all competitions.
* */
public class ReadFromFile {


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
        String[] splittedBuffer = new String[50];
        System.out.println("Extracted Line = ");
        splittedBuffer = buffer.split(",");
        for (String i : splittedBuffer) {
            System.out.println("-> " + i);
        }

    }
}