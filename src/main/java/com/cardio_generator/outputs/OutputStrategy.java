package com.cardio_generator.outputs;

/**
 * Interface that all output strategies must implement.
 * This is how the system stays flexible about where data gets sent,
 * whether thats a file, console, or a network connection.
 */
public interface OutputStrategy {

    /**
     * Takes a single patient data record and sends it somewhere.
     * The exact destination depends on which implementation is being used.
     *
     * @param patientId which patient this data belongs to
     * @param timestamp when the data was recorded, as milliseconds since epoch
     * @param label the kind of measurement this is, for example "ECG" or "Saturation"
     * @param data the actual value as a string
     */
    void output(int patientId, long timestamp, String label, String data);
}
