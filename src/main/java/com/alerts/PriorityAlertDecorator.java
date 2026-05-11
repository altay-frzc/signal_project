package com.alerts;

/**
 * Decorator that adds a priority level to an alert.
 * Used when an alert needs urgent attention.
 */
public class PriorityAlertDecorator extends AlertDecorator {

    private String priority;

    /**
     * Creates a priority alert decorator.
     *
     * @param decoratedAlert the alert to wrap
     * @param priority the priority level, e.g. "HIGH" or "CRITICAL"
     */
    public PriorityAlertDecorator(Alert decoratedAlert, String priority) {
        super(decoratedAlert);
        this.priority = priority;
    }

    /**
     * Returns the condition with priority tag added.
     *
     * @return condition string with priority level
     */
    @Override
    public String getCondition() {
        return "[" + priority + "] " + decoratedAlert.getCondition();
    }
}
