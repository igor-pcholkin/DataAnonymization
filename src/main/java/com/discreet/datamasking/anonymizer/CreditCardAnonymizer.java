package com.discreet.datamasking.anonymizer;

import static java.lang.Character.isDigit;

/**
 * Anonymizer which works with credit cards.
 *
 * Example: "5209761132208795" -> "8686222710866319"
 */
public class CreditCardAnonymizer extends CharSequenceAnonymizer {

    @Override
    protected boolean isTranslationNeeded(int origCodePoint, String input, int i) {
        return isDigit(origCodePoint);
    }

    @Override
    protected Character doTranslateChar(int origCodePoint, String input, int i) {
        int translatedCodePoint = createDigit(origCodePoint);
        return (char) translatedCodePoint;
    }
}
