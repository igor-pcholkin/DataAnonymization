package com.discreet.dataprotection.anonymizer;

import static java.lang.Character.isSpaceChar;
import static java.lang.Character.toTitleCase;

/**
 * Anonymizer which changes email to a randomly looking one.
 *
 * Example:
 */
public class EmailAnonymizer extends CharSequenceAnonymizer {

    @Override
    protected boolean isTranslationNeeded(int origCodePoint, String input, int i) {
        return i < input.lastIndexOf('.');
    }

}
