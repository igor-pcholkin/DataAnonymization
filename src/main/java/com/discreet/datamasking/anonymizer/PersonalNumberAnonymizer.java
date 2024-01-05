package com.discreet.datamasking.anonymizer;

import java.util.Random;

/**
 * A simplistic "one-size-fits-all" anonymizer which can work with different personal codes
 * /social security numbers/tax payer numbers.
 * Ignores country-specific rules, checksums and delimiters.
 */
public class PersonalNumberAnonymizer extends AbstractAnonymizer {
    static final int BASE_DIGIT = '0';
    static final int DIGIT_RANGE = 10;

    @Override
    protected boolean isTranslationNeeded(int origCodePoint, String input, int i) {
        return Character.isDigit(origCodePoint);
    }

    @Override
    protected Character doTranslateChar(int origCodePoint, Random random, String input, int i) {
        int translatedCodePoint = BASE_DIGIT + (Math.abs(random.nextInt()) % DIGIT_RANGE);
        return Character.valueOf((char) translatedCodePoint);
    }
}
