package com.discreet.datamasking.anonymizer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Anonymizer who generates birth dates. Actually can be used for dates in general.
 *
 * Example: "2024-01-06" -> "1937-10-22"
 */
public class BirthDateAnonymizer implements Anonymizer {
    private LocalDate minDate;
    private LocalDate maxDate;

    static final LocalDate DEFAULT_MIN_LOCAL_DATE = LocalDate.of(1900, 1, 1);
    static final LocalDate DEFAULT_MAX_LOCAL_DATE = LocalDate.of(2024, 1, 1);

    public BirthDateAnonymizer() {
        minDate = DEFAULT_MIN_LOCAL_DATE;
        maxDate = DEFAULT_MAX_LOCAL_DATE;
    }

    public BirthDateAnonymizer(LocalDate minDate, LocalDate maxDate) {
        this.minDate = minDate;
        this.maxDate = maxDate;
    }

    @Override
    public String anonymize(String input) {
        return createLocalDate().toString();
    }

    private LocalDate createLocalDate() {
        LocalDateTime minDateTime = getLocalDateTime(minDate);
        LocalDateTime maxDateTime = getLocalDateTime(maxDate);
        long daysBetween = Duration.between(minDateTime, maxDateTime).toDays();
        long randomDay = new Random().nextLong(daysBetween);
        return minDate.plusDays(randomDay);
    }

    private LocalDateTime getLocalDateTime(LocalDate localDate) {
        return LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(),
                0,0,0);
    }
}
