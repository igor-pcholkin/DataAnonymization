package com.discreet.datamasking.anonymizer;

public interface Anonymizer {
    /**
     * Returns anonymous value which will keep the same pattern as input value.
     * Output value always depends on input value so if input is the same for multiple invocations then
     * output should be the same.
     *
     * @param input
     * @return
     */
    String anonymize(String input);
}
