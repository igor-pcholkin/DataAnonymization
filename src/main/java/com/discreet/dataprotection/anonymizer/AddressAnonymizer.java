package com.discreet.dataprotection.anonymizer;

import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isDigit;

/**
 * Anonymizer which works with addresses.
 * Works similar way as personal number anonymizer:
 * translates alphabetic letters to alphabetic letters (respecting case) and digits to digits,
 * leaving other characters in place.
 *
 * Example: "1 Arcadia Lane\nAnnandale, VA 22003" -> "2 Gizwndh Cyww\nZpduyalab, XG 64883"
 */
public class AddressAnonymizer extends CharSequenceAnonymizer {

    @Override
    protected boolean isTranslationNeeded(int origCodePoint, String input, int i) {
        return isDigit(origCodePoint) || isAlphabetic(origCodePoint);
    }


}
