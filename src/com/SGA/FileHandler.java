package com.SGA;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;

public class FileHandler {
    private String filePath = null;
    private String fileContent = null;

    // Default constructor
    public FileHandler() {
    }

    // Constructor with file path
    public FileHandler(String filePath) {
        this.filePath = filePath;
    }

    // If file has been read (!= Null), return file content
    public String getFileContent() {
        return Objects.requireNonNullElse(this.fileContent, "File not read");
    }

    // Read file from path
    public void readFileFromPath(String filePath) {
        try {
            Path path = Path.of(filePath);
            this.fileContent = new String(Files.readAllBytes(path));
            this.filePath = filePath;
        } catch (InvalidPathException | NoSuchFileException e) {
            System.out.println("Invalid path: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Write content to file
    // If file does not exist, create it
    // If file exists, overwrite it
    public void writeFileToPath(String filePath, String fileContent) {
        try {
            Path path = Path.of(filePath);
            Files.write(path, fileContent.getBytes());
        } catch (InvalidPathException e) {
            System.out.println("Invalid path: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("File Busy: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
