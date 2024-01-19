package com.discreet.datamasking.anonymizer;

import java.util.List;

/**
 * "Shuffing" Anonymizer for post codes.
 *
 * Tries to use random code from pool of correct post codes.
 */
public class PostCodeAnonymizer extends BaseAnonymizer {

    final private List<String> postCodes;

    public PostCodeAnonymizer(List<String> postCodes) {
        this.postCodes = postCodes;
    }

    @Override
    public String anonymize(String input) {
        initRandom(input.hashCode());
        return postCodes.get(getRandom().nextInt(postCodes.size()));
    }
}
