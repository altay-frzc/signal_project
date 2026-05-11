package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;

/**
 * Interface for all alert strategies.
 * Each strategy encapsulates the logic for checking
 * a specific type of health metric.
 */
public interface AlertStrategy {

    /**
     * Checks whether an alert should be triggered for the given patient.
     *
     * @param patient the patient to check
     * @param dataStorage the storage to retrieve patient records from
     */
    void checkAlert(Patient patient, DataStorage dataStorage);
}