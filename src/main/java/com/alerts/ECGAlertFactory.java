package com.alerts;

/**
 * Factory that creates ECG related alerts.
 */
public class ECGAlertFactory extends AlertFactory {

    /**
     * Creates an ECG alert for the given patient.
     *
     * @param patientId the id of the patient
     * @param condition the ECG condition detected
     * @param timestamp when the alert was triggered
     * @return a new Alert for ECG
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new Alert(patientId, "ECG: " + condition, timestamp);
    }
}