package com.discreet.datamasking.anonymizer;

import java.util.Random;

public abstract class BaseAnonymizer implements Anonymizer {
    private static final ThreadLocal<Random> RANDOM_THREAD_LOCAL = ThreadLocal.withInitial(Random::new);

    protected Random initRandom(int seed) {
        Random random = getRandom();
        random.setSeed(seed);
        return random;
    }

    protected Random getRandom() {
        return RANDOM_THREAD_LOCAL.get();
    }
}
