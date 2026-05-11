package com.alerts;

/**
 * Factory that creates blood oxygen related alerts.
 */
public class BloodOxygenAlertFactory extends AlertFactory {

    /**
     * Creates a blood oxygen alert for the given patient.
     *
     * @param patientId the id of the patient
     * @param condition the oxygen condition detected
     * @param timestamp when the alert was triggered
     * @return a new Alert for blood oxygen
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new Alert(patientId, "BloodOxygen: " + condition, timestamp);
    }
}