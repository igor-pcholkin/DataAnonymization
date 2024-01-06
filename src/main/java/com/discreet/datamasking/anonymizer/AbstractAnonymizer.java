package com.discreet.datamasking.anonymizer;

import java.util.Random;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLowerCase;
import static java.lang.Character.isUpperCase;

public abstract class AbstractAnonymizer implements Anonymizer {
    protected static final int BASE_DIGIT = '0';
    protected static final int DIGIT_RANGE = 10;
    protected static final int LATIN_CHAR_RANGE = 26;
    protected static final int LATIN_BASE_CHAR_LOWER = 'a';
    protected static final int LATIN_BASE_CHAR_UPPER = 'A';

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

    protected Character translateChar(String input, int i, Random random) {
        int origCodePoint = input.codePointAt(i);
        return isTranslationNeeded(origCodePoint, input, i) ? doTranslateChar(origCodePoint, random, input, i) :
                (char) origCodePoint;
    }

    protected int createDigit(Random random) {
        return createChar(BASE_DIGIT, DIGIT_RANGE, random);
    }

    protected int createLowerCaseChar(Random random) {
        return createChar(LATIN_BASE_CHAR_LOWER, LATIN_CHAR_RANGE, random);
    }

    protected int createUpperCaseChar(Random random) {
        return createChar(LATIN_BASE_CHAR_UPPER, LATIN_CHAR_RANGE, random);
    }

    protected int createChar(int charBase, int charRange, Random random) {
        return charBase + ( Math.abs(random.nextInt()) % charRange );
    }

    protected int createChar(int origCodePoint, Random random) {
        if (isLowerCase(origCodePoint))
            return createLowerCaseChar(random);
        else if (isUpperCase(origCodePoint))
            return createUpperCaseChar(random);
        if (isDigit(origCodePoint))
            return createDigit(random);
        else throw new RuntimeException("Cannot create character from unknown character type!");
    }

    protected abstract boolean isTranslationNeeded(int origCodePoint, String input, int i);
    protected abstract Character doTranslateChar(int origCodePoint, Random random, String input, int i);
}
