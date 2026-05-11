package data_management;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import com.data_management.WebSocketClientImpl;

public class WebSocketClientTest {

    private DataStorage storage;
    private WebSocketClientImpl client;

    @BeforeEach
    void setUp() throws Exception {
        java.lang.reflect.Field field = DataStorage.class.getDeclaredField("instance");
        field.setAccessible(true);
        field.set(null, null);
        storage = DataStorage.getInstance();
        client = new WebSocketClientImpl("ws://localhost:8080", storage);
    }

    @Test
    void testValidMessageParsing() {
        // Simulate receiving a valid message
        client.onMessage("1,1000,HeartRate,72.0");
        List<PatientRecord> records = storage.getRecords(1, 0L, 9999999999999L);
        assertEquals(1, records.size());
        assertEquals(72.0, records.get(0).getMeasurementValue());
        assertEquals("HeartRate", records.get(0).getRecordType());
    }

    @Test
    void testInvalidMessageIgnored() {
        // Simulate receiving an invalid message
        client.onMessage("invalid_message");
        List<PatientRecord> records = storage.getRecords(1, 0L, 9999999999999L);
        assertTrue(records.isEmpty());
    }

    @Test
    void testCorruptedDataIgnored() {
        // Simulate receiving corrupted data
        client.onMessage("abc,xyz,HeartRate,notanumber");
        List<PatientRecord> records = storage.getRecords(1, 0L, 9999999999999L);
        assertTrue(records.isEmpty());
    }

    @Test
    void testMultipleMessages() {
        client.onMessage("1,1000,HeartRate,72.0");
        client.onMessage("1,2000,HeartRate,75.0");
        client.onMessage("1,3000,HeartRate,80.0");
        List<PatientRecord> records = storage.getRecords(1, 0L, 9999999999999L);
        assertEquals(3, records.size());
    }

    @Test
    void testDifferentPatients() {
        client.onMessage("1,1000,HeartRate,72.0");
        client.onMessage("2,1000,HeartRate,80.0");
        List<PatientRecord> patient1 = storage.getRecords(1, 0L, 9999999999999L);
        List<PatientRecord> patient2 = storage.getRecords(2, 0L, 9999999999999L);
        assertEquals(1, patient1.size());
        assertEquals(1, patient2.size());
    }

    @Test
    void testMissingFieldsIgnored() {
        client.onMessage("1,1000,HeartRate");
        List<PatientRecord> records = storage.getRecords(1, 0L, 9999999999999L);
        assertTrue(records.isEmpty());
    }
}