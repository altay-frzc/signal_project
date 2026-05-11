package com.alerts;

/**
 * Decorator that marks an alert as repeated.
 * Used when the same condition has been triggered multiple times.
 */
public class RepeatedAlertDecorator extends AlertDecorator {

    private int repeatCount;

    /**
     * Creates a repeated alert decorator.
     *
     * @param decoratedAlert the alert to wrap
     * @param repeatCount how many times the alert has been repeated
     */
    public RepeatedAlertDecorator(Alert decoratedAlert, int repeatCount) {
        super(decoratedAlert);
        this.repeatCount = repeatCount;
    }

    /**
     * Returns the condition with repeat info added.
     *
     * @return condition string with repeat count
     */
    @Override
    public String getCondition() {
        return decoratedAlert.getCondition() + " [Repeated: " + repeatCount + " times]";
    }
}