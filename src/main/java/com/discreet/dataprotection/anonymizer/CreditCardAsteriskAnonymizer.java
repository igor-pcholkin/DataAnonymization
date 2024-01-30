package com.discreet.dataprotection.anonymizer;


/**
 * Anonymizer which works with credit cards.
 * Keeps most starting digits in place except trailing n ones which are masked by asterisk.
 *
 * Example: "5209761132208795" -> "520976113220****"
 */
public class CreditCardAsteriskAnonymizer extends CharSequenceAnonymizer {

    private int numTrailingCharsToMask = 4;

    public CreditCardAsteriskAnonymizer() {
        //
    }

    public CreditCardAsteriskAnonymizer(int numTrailingCharsToMask) {
        this.numTrailingCharsToMask = numTrailingCharsToMask;
    }

    @Override
    protected boolean isTranslationNeeded(int origCodePoint, String input, int i) {
        return i >= input.length() - numTrailingCharsToMask;
    }

    @Override
    protected Character doTranslateChar(int origCodePoint, String input, int i) {
        return '*';
    }
}
