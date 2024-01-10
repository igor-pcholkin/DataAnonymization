package com.discreet.datamasking.anonymizer;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLowerCase;
import static java.lang.Character.isUpperCase;

/**
 * Anonymizer which replaces data character by character
 */
public abstract class CharSequenceAnonymizer extends BaseAnonymizer {
    protected static final int BASE_DIGIT = '0';
    protected static final int DIGIT_RANGE = 10;
    protected static final int LATIN_CHAR_RANGE = 26;
    protected static final int LATIN_BASE_CHAR_LOWER = 'a';
    protected static final int LATIN_BASE_CHAR_UPPER = 'A';

    @Override
    public String anonymize(String input) {
        initRandom(input.hashCode());
        StringBuilder outputStringBuilder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            Character translatedChar = translateChar(input, i);
            outputStringBuilder.append(translatedChar);
        }
        return outputStringBuilder.toString();
    }

    protected Character translateChar(String input, int i) {
        int origCodePoint = input.codePointAt(i);
        return isTranslationNeeded(origCodePoint, input, i) ? doTranslateChar(origCodePoint, input, i) :
                (char) origCodePoint;
    }

    protected int translateDigit(int origCodePoint) {
        return translateChar(origCodePoint, BASE_DIGIT, DIGIT_RANGE);
    }

    protected int translateLowerCaseChar(int origCodePoint) {
        return translateChar(origCodePoint, LATIN_BASE_CHAR_LOWER, LATIN_CHAR_RANGE);
    }

    protected int translateUpperCaseChar(int origCodePoint) {
        return translateChar(origCodePoint, LATIN_BASE_CHAR_UPPER, LATIN_CHAR_RANGE);
    }

    protected int translateChar(int origCodePoint, int charBase, int charRange) {
        return charBase + ( (origCodePoint + Math.abs(getRandom().nextInt())) % charRange );
    }

    protected int translateChar(int origCodePoint) {
        if (isLowerCase(origCodePoint))
            return translateLowerCaseChar(origCodePoint);
        else if (isUpperCase(origCodePoint))
            return translateUpperCaseChar(origCodePoint);
        if (isDigit(origCodePoint))
            return translateDigit(origCodePoint);
        else
            return origCodePoint;
    }

    protected Character doTranslateChar(int origCodePoint, String input, int i) {
        int translatedCodePoint = translateChar(origCodePoint);
        return (char) translatedCodePoint;
    }

    protected abstract boolean isTranslationNeeded(int origCodePoint, String input, int i);
}
