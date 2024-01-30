package com.discreet.dataprotection.anonymizer;

import java.util.Random;

public abstract class BaseAnonymizer implements Anonymizer {
    private static final ThreadLocal<Random> RANDOM_THREAD_LOCAL = ThreadLocal.withInitial(Random::new);
    private static final int FIXED_SEED = 1234;

    protected Random initRandom(int seed) {
        Random random = getRandom();
        random.setSeed(seed);
        return random;
    }

    public Random initRandom() {
        Random random = getRandom();
        random.setSeed(FIXED_SEED);
        return random;
    }

    protected Random getRandom() {
        return RANDOM_THREAD_LOCAL.get();
    }
}
