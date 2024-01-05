package com.discreet.datamasking.anonymizer;

import com.discreet.datamasking.core.Anonymizer;

import java.util.Random;

/**
 * Anonymizer working with names/full names.
 * Substitutes each letter with a random latin letter except starting (capital) letters
 * Respects case and number of original letters in each name.
 * Also delimiting spaces are kept in place.
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
        return isTranslationNeeded(origCodePoint, input, i) ? doTranslateChar(origCodePoint, random) :
                Character.valueOf((char) origCodePoint);
    }

    private boolean isTranslationNeeded(int origCodePoint, String input, int i) {
        return !(Character.isSpaceChar(origCodePoint) || isValidFirstLetterInWord(origCodePoint, input, i));
    }

    private boolean isValidFirstLetterInWord(int origCodePoint, String input, int i) {
        return (i == 0 || Character.isSpaceChar(input.codePointAt(i - 1))) &&
                Character.isAlphabetic(origCodePoint);
    }

    private Character doTranslateChar(int origChar, Random random) {
        int baseChar = Character.isLowerCase(origChar) ? LATIN_BASE_CHAR_LOWER : LATIN_BASE_CHAR_UPPER;
        return Character.valueOf((char) (baseChar + ( (origChar + Math.abs(random.nextInt())) % LATIN_CHAR_RANGE )));
    }
}
