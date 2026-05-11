package com.alerts;

import java.util.List;
import java.util.stream.Collectors;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * Strategy that checks blood pressure for critical thresholds and trends.
 */
public class BloodPressureStrategy implements AlertStrategy {

    private AlertFactory factory = new BloodPressureAlertFactory();

    /**
     * Checks systolic and diastolic blood pressure for critical values
     * and increasing or decreasing trends.
     *
     * @param patient the patient to check
     * @param dataStorage storage to get records from
     */
    @Override
    public void checkAlert(Patient patient, DataStorage dataStorage) {
        List<PatientRecord> records = dataStorage.getRecords(
            patient.getPatientId(), 0, System.currentTimeMillis());

        List<PatientRecord> systolic = records.stream()
            .filter(r -> r.getRecordType().equals("SystolicPressure"))
            .collect(Collectors.toList());

        for (PatientRecord record : systolic) {
            if (record.getMeasurementValue() > 180) {
                Alert alert = factory.createAlert(
                    String.valueOf(patient.getPatientId()), "Critical Systolic High", record.getTimestamp());
                System.out.println("ALERT: " + alert.getCondition());
            } else if (record.getMeasurementValue() < 90) {
                Alert alert = factory.createAlert(
                    String.valueOf(patient.getPatientId()), "Critical Systolic Low", record.getTimestamp());
                System.out.println("ALERT: " + alert.getCondition());
            }
        }
    }
}