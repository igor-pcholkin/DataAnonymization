package com.discreet.datamasking.anonymizer;

import java.util.Random;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLowerCase;
import static java.lang.Character.isUpperCase;

/**
 * Anonymizer which replaces data character by character
 */
public abstract class CharSequenceAnonymizer implements Anonymizer {
    protected static final int BASE_DIGIT = '0';
    protected static final int DIGIT_RANGE = 10;
    protected static final int LATIN_CHAR_RANGE = 26;
    protected static final int LATIN_BASE_CHAR_LOWER = 'a';
    protected static final int LATIN_BASE_CHAR_UPPER = 'A';

    @Override
    public String anonymize(String input) {
        Random random = new Random();
        random.setSeed(input.hashCode());
        StringBuilder outputStringBuilder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            Character translatedChar = translateChar(input, i, random);
            outputStringBuilder.append(translatedChar);
        }
        return outputStringBuilder.toString();
    }

    protected Character translateChar(String input, int i, Random random) {
        int origCodePoint = input.codePointAt(i);
        return isTranslationNeeded(origCodePoint, input, i) ? doTranslateChar(origCodePoint, random, input, i) :
                (char) origCodePoint;
    }

    protected int createDigit(int origCodePoint, Random random) {
        return translateChar(origCodePoint, BASE_DIGIT, DIGIT_RANGE, random);
    }

    protected int createLowerCaseChar(int origCodePoint, Random random) {
        return translateChar(origCodePoint, LATIN_BASE_CHAR_LOWER, LATIN_CHAR_RANGE, random);
    }

    protected int createUpperCaseChar(int origCodePoint, Random random) {
        return translateChar(origCodePoint, LATIN_BASE_CHAR_UPPER, LATIN_CHAR_RANGE, random);
    }

    protected int translateChar(int origCodePoint, int charBase, int charRange, Random random) {
        return charBase + ( (origCodePoint + Math.abs(random.nextInt())) % charRange );
    }

    protected int translateChar(int origCodePoint, Random random) {
        if (isLowerCase(origCodePoint))
            return createLowerCaseChar(origCodePoint, random);
        else if (isUpperCase(origCodePoint))
            return createUpperCaseChar(origCodePoint, random);
        if (isDigit(origCodePoint))
            return createDigit(origCodePoint, random);
        else throw new RuntimeException("Cannot create character from unknown character type!");
    }

    protected abstract boolean isTranslationNeeded(int origCodePoint, String input, int i);
    protected abstract Character doTranslateChar(int origCodePoint, Random random, String input, int i);
}
