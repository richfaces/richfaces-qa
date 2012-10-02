package org.richfaces.tests.metamer.bean.issues.rf12291;

public interface InitAction {

    /**
     * Returns the initial navigation outcome
     */
    String getNavigationOutcome();

    /**
     * Returns the path to the include for this action
     */
    String getIncludePath();

    /**
     * Initialises the corresponding action
     *
     * @param initArg argument to pass in into init method
     */
    void init(Object initArg);
}
