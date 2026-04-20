package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Writes patient data to text files on disk. Each type of measurement
 * gets its own file inside the base directory. If the file doesnt exist
 * yet it will be created, otherwise data gets appended to it.
 */
public class FileOutputStrategy implements OutputStrategy { //change from fileOutputStrategy to FileOutputStrategy to match the class name

    private String baseDirectory; //change from BaseDirectory to baseDirectory to follow Java naming conventions

    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>(); //change from file_map to fileMap to follow Java naming conventions


     /**
     * Sets up the strategy with a base directory where all files will go.
     *
     * @param baseDirectory path to the folder where output files should be created
     */
    public FileOutputStrategy(String baseDirectory) { 

        this.baseDirectory = baseDirectory;
    }

       /**
     * Writes one line of patient data to the appropriate file.
     * The file is determined by the label parameter. If the base
     * directory doesnt exist it will try to create it first.
     *
     * @param patientId the patient this record belongs to
     * @param timestamp time of the measurement in milliseconds
     * @param label measurement type, also used as the file name
     * @param data the value to write
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        String filePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString()); //change from FilePath to filePath to follow Java naming conventions

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}