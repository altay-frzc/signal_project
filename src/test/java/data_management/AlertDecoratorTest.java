package data_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.alerts.Alert;
import com.alerts.PriorityAlertDecorator;
import com.alerts.RepeatedAlertDecorator;

public class AlertDecoratorTest {

    @Test
    void testRepeatedAlertDecorator() {
        Alert alert = new Alert("1", "High Systolic", 1000L);
        RepeatedAlertDecorator repeated = new RepeatedAlertDecorator(alert, 3);
        assertTrue(repeated.getCondition().contains("High Systolic"));
        assertTrue(repeated.getCondition().contains("Repeated"));
        assertTrue(repeated.getCondition().contains("3"));
    }

    @Test
    void testPriorityAlertDecorator() {
        Alert alert = new Alert("1", "Low Saturation", 2000L);
        PriorityAlertDecorator priority = new PriorityAlertDecorator(alert, "HIGH");
        assertTrue(priority.getCondition().contains("Low Saturation"));
        assertTrue(priority.getCondition().contains("HIGH"));
    }

    @Test
    void testPatientIdPreserved() {
        Alert alert = new Alert("1", "ECG Abnormal", 1000L);
        RepeatedAlertDecorator repeated = new RepeatedAlertDecorator(alert, 2);
        assertEquals("1", repeated.getPatientId());
    }

    @Test
    void testTimestampPreserved() {
        Alert alert = new Alert("1", "ECG Abnormal", 5000L);
        PriorityAlertDecorator priority = new PriorityAlertDecorator(alert, "CRITICAL");
        assertEquals(5000L, priority.getTimestamp());
    }

    @Test
    void testCombinedDecorators() {
        Alert alert = new Alert("1", "High Systolic", 1000L);
        Alert repeated = new RepeatedAlertDecorator(alert, 2);
        Alert priority = new PriorityAlertDecorator(repeated, "CRITICAL");
        assertTrue(priority.getCondition().contains("CRITICAL"));
        assertTrue(priority.getCondition().contains("High Systolic"));
    }
}