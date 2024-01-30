package com.discreet.dataprotection.anonymizer;

import static java.lang.Character.toTitleCase;
import static java.lang.Character.isSpaceChar;
import static java.lang.Character.isAlphabetic;

/**
 * Anonymizer working with names/full names.
 * Substitutes each letter with a random latin letter except starting ones
 * Letters converted to appropriate case: starting ones to upper case, following ones to lower case.
 * Respects number of original letters in each name.
 *
 * Example: "John Paul Smith" -> "Jzir Pyre Snpex"
 */
public class FullNameAnonymizer extends CharSequenceAnonymizer {

    @Override
    protected boolean isTranslationNeeded(int origCodePoint, String input, int i) {
        return !isSpaceChar(origCodePoint);
    }

    @Override
    protected Character doTranslateChar(int origCodePoint, String input, int i) {
        int translatedCodePoint = isValidFirstLetterInWord(origCodePoint, input, i) ? toTitleCase(origCodePoint) :
                translateLowerCaseChar(origCodePoint);
        return (char) translatedCodePoint;
    }

    protected boolean isValidFirstLetterInWord(int origCodePoint, String input, int i) {
        return (i == 0 || isSpaceChar(input.codePointAt(i - 1)))
                && isAlphabetic(origCodePoint);
    }

}
