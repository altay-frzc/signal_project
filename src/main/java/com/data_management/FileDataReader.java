package com.data_management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Reads patient data from text files in a specified directory
 * and stores it in DataStorage. Each line in the files should follow
 * the format: patientId,timestamp,label,data
 */
public class FileDataReader implements DataReader {

    private String directory;

    /**
     * Creates a FileDataReader that reads from the given directory.
     *
     * @param directory path to the folder containing the data files
     */
    public FileDataReader(String directory) {
        this.directory = directory;
    }

    /**
     * Reads all files in the directory and stores the parsed data in DataStorage.
     * Skips lines that dont match the expected format.
     *
     * @param dataStorage the storage where parsed data will be saved
     * @throws IOException if the directory cant be read
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            paths.filter(Files::isRegularFile)
                 .forEach(path -> {
                     try {
                         parseFile(path, dataStorage);
                     } catch (IOException e) {
                         System.err.println("Error reading file: " + path);
                     }
                 });
        }
    }

    /**
     * Parses a single file and adds each record to DataStorage.
     *
     * @param path the file to parse
     * @param dataStorage where to store the parsed records
     * @throws IOException if the file cant be read
     */
    private void parseFile(Path path, DataStorage dataStorage) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    // Expected format: Patient ID: 1, Timestamp: 123456, Label: HeartRate, Data: 72.0
                    String[] parts = line.split(", ");
                    int patientId = Integer.parseInt(parts[0].split(": ")[1].trim());
                    long timestamp = Long.parseLong(parts[1].split(": ")[1].trim());
                    String label = parts[2].split(": ")[1].trim();
                    double data = Double.parseDouble(parts[3].split(": ")[1].trim());
                    dataStorage.addPatientData(patientId, data, label, timestamp);
                } catch (Exception e) {
                    System.err.println("Skipping invalid line: " + line);
                }
            }
        }
    }
}