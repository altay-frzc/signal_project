package data_management;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

public class DataStorageTest {

    private DataStorage storage;

    @BeforeEach
    void setUp() {
    storage = new DataStorage();
}
    @Test
    void testAddAndGetRecords() {
        // TODO Perhaps you can implement a mock data reader to mock the test data?
        // DataReader reader
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);

        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(2, records.size()); // Check if two records are retrieved
        assertEquals(100.0, records.get(0).getMeasurementValue()); // Validate first record
    }

    @Test
void testGetRecordsEmptyWhenNoPatient() {
    List<PatientRecord> records = storage.getRecords(99, 0L, 9999999999999L);
    assertTrue(records.isEmpty());
}

@Test
void testGetRecordsFiltersByTime() {
    storage.addPatientData(1, 100.0, "HeartRate", 1000L);
    storage.addPatientData(1, 200.0, "HeartRate", 2000L);
    storage.addPatientData(1, 300.0, "HeartRate", 3000L);
    List<PatientRecord> records = storage.getRecords(1, 1500L, 2500L);
    assertEquals(1, records.size());
    assertEquals(200.0, records.get(0).getMeasurementValue());
}

@Test
void testGetAllPatients() {
    storage.addPatientData(1, 100.0, "HeartRate", 1000L);
    storage.addPatientData(2, 200.0, "HeartRate", 2000L);
    assertEquals(2, storage.getAllPatients().size());
}

@Test
void testAddMultipleRecordTypes() {
    storage.addPatientData(1, 120.0, "SystolicPressure", 1000L);
    storage.addPatientData(1, 95.0, "Saturation", 2000L);
    List<PatientRecord> records = storage.getRecords(1, 0L, 9999999999999L);
    assertEquals(2, records.size());
}
}
