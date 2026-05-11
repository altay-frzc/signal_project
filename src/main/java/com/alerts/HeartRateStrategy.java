package com.alerts;

import java.util.List;
import java.util.stream.Collectors;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * Strategy that checks heart rate for abnormal values.
 */
public class HeartRateStrategy implements AlertStrategy {

    private AlertFactory factory = new ECGAlertFactory();

    /**
     * Checks ECG records for abnormal heart rate values.
     * Triggers alert if heart rate is above 100 or below 50.
     *
     * @param patient the patient to check
     * @param dataStorage storage to get records from
     */
    @Override
    public void checkAlert(Patient patient, DataStorage dataStorage) {
        List<PatientRecord> records = dataStorage.getRecords(
            patient.getPatientId(), 0, System.currentTimeMillis());

        List<PatientRecord> ecgRecords = records.stream()
            .filter(r -> r.getRecordType().equals("ECG"))
            .collect(Collectors.toList());

        for (PatientRecord record : ecgRecords) {
            if (record.getMeasurementValue() > 100 || record.getMeasurementValue() < 50) {
                Alert alert = factory.createAlert(
                    String.valueOf(patient.getPatientId()), "Abnormal Heart Rate", record.getTimestamp());
                System.out.println("ALERT: " + alert.getCondition());
            }
        }
    }
}