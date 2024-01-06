package com.discreet.datamasking.anonymizer.name;

import java.util.Random;

/**
 * Anonymizer working with names/full names.
 * Substitutes each letter with a random latin letter.
 * Letters converted to appropriate case: starting ones to upper case, following ones to lower case.
 * Respects number of original letters in each name.
 *
 * Example: "John Paul Smith" -> "Hvwx Ptym Erpbf"
 */
public class FullNameRandomAnonymizer extends FullNameMildAnonymizer {
    @Override
    protected Character doTranslateChar(int origCodePoint, Random random, String input, int i) {
        int translatedCodePoint = isValidFirstLetterInWord(origCodePoint, input, i) ? createUpperCaseChar(random) :
                createLowerCaseChar(random);
        return (char) translatedCodePoint;
    }

}
