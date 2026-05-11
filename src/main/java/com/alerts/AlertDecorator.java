package com.alerts;

/**
 * Base decorator class for alerts. Wraps an existing alert
 * and allows adding extra behaviour without changing the original.
 */
public abstract class AlertDecorator extends Alert {

    protected Alert decoratedAlert;

    /**
     * Creates a decorator wrapping the given alert.
     *
     * @param decoratedAlert the alert to wrap
     */
    public AlertDecorator(Alert decoratedAlert) {
        super(decoratedAlert.getPatientId(), decoratedAlert.getCondition(), decoratedAlert.getTimestamp());
        this.decoratedAlert = decoratedAlert;
    }

    /**
     * Returns the condition of the wrapped alert.
     *
     * @return the condition string
     */
    @Override
    public String getCondition() {
        return decoratedAlert.getCondition();
    }
}