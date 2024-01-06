package com.discreet.datamasking.anonymizer;

import java.util.Random;

import static java.lang.Character.isDigit;
import static java.lang.Character.isAlphabetic;

/**
 * A simplistic "one-size-fits-all" anonymizer which can work with different personal codes
 * /social security numbers/tax payer numbers.
 * Ignores country-specific rules, checksums and delimiters.
 * Disregards birth day specific properties.
 *
 * Example: "020976-11322" -> "863278-65211"
 */
public class PersonalNumberAnonymizer extends CharSequenceAnonymizer {

    @Override
    protected boolean isTranslationNeeded(int origCodePoint, String input, int i) {
        return isDigit(origCodePoint) || isAlphabetic(origCodePoint);
    }

    @Override
    protected Character doTranslateChar(int origCodePoint, Random random, String input, int i) {
        int translatedCodePoint = createChar(origCodePoint, random);
        return (char) translatedCodePoint;
    }
}
