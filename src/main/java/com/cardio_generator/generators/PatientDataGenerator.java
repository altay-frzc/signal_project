package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Interface for all the data generators in the system.
 * Any class that wants to generate patient data should implement this,
 * it makes sure all generators follow the same basic structure.
 */
public interface PatientDataGenerator {

    /**
     * Generates one data point for the specified patient and sends it
     * to the output. What kind of data gets generated depends on
     * which class implements this.
     *
     * @param patientId the patient we are generating data for
     * @param outputStrategy the output where the data will be sent to
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
