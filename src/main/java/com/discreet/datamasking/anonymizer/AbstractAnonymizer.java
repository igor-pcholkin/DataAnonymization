package com.discreet.datamasking.anonymizer;

import java.util.Random;

public abstract class AbstractAnonymizer implements Anonymizer {
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
                Character.valueOf((char) origCodePoint);
    }

    protected abstract boolean isTranslationNeeded(int origCodePoint, String input, int i);
    protected abstract Character doTranslateChar(int origCodePoint, Random random, String input, int i);
}
