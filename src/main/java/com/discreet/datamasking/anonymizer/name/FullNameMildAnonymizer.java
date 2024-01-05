package com.discreet.datamasking.anonymizer.name;

import com.discreet.datamasking.core.Anonymizer;

import java.util.Random;

/**
 * Anonymizer working with names/full names.
 * Substitutes each letter with a random latin letter except starting ones
 * Letters converted to appropriate case: starting ones to upper case, following ones to lower case.
 * Respects number of original letters in each name.
 *
 * Example: "John Paul Smith" -> "Jzir Pyre Snpex"
 */
public class FullNameMildAnonymizer implements Anonymizer {
    static final int LATIN_CHAR_RANGE = 26;
    static final int LATIN_BASE_CHAR_LOWER = 'a';
    static final int LATIN_BASE_CHAR_UPPER = 'A';

    @Override
    public String anonymize(String input) {
        Random random = new Random();
        StringBuilder outputStringBuilder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            Character translatedChar = translateChar(input, i, random);
            outputStringBuilder.append(translatedChar);
        }
        return outputStringBuilder.toString();
    }

    private Character translateChar(String input, int i, Random random) {
        int origCodePoint = input.codePointAt(i);
        return isTranslationNeeded(origCodePoint, input, i) ? doTranslateChar(origCodePoint, random, input, i) :
                Character.valueOf((char) origCodePoint);
    }

    protected boolean isTranslationNeeded(int origCodePoint, String input, int i) {
        return !Character.isSpaceChar(origCodePoint);
    }

    protected boolean isValidFirstLetterInWord(int origCodePoint, String input, int i) {
        return (i == 0 || Character.isSpaceChar(input.codePointAt(i - 1))) &&
                Character.isAlphabetic(origCodePoint);
    }

    protected Character doTranslateChar(int origCodePoint, Random random, String input, int i) {
        int maskedCodePoint = isValidFirstLetterInWord(origCodePoint, input, i) ? Character.toTitleCase(origCodePoint) :
                LATIN_BASE_CHAR_LOWER + ( (origCodePoint + Math.abs(random.nextInt())) % LATIN_CHAR_RANGE );
        return Character.valueOf((char) maskedCodePoint);
    }
}
