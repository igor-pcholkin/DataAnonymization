package com.discreet.datamasking.anonymizer;

import java.util.List;
import java.util.Set;

/**
 * "Shuffing" Anonymizer for post codes.
 *
 * Tries to use random code from pool of correct post codes.
 */
public class PostCodeAnonymizer extends BaseAnonymizer {

    final private List<String> postCodes;

    public PostCodeAnonymizer(Set<String> postCodes) {
        this.postCodes = postCodes.stream().toList();
    }

    @Override
    public String anonymize(String input) {
        initRandom(input.hashCode());
        return postCodes.get(getRandom().nextInt(postCodes.size()));
    }
}
