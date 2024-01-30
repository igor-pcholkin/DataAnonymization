package com.discreet.dataprotection.anonymizer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Anonymizer generating dates (with times)
 *
 * Example: "2024-01-06T11:55:28" -> "1950-08-14T05:02:13"
 *
 * NB. There is no guarantee that length of the input will match length of output as in both cases
 * seconds could be omitted when parsing/formatting.
 */
public class DateTimeAnonymizer extends BaseAnonymizer {
    private LocalDateTime minDateTime;
    private LocalDateTime maxDateTime;

    static final LocalDateTime DEFAULT_MIN_LOCAL_DATE_TIME = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
    static final LocalDateTime DEFAULT_MAX_LOCAL_DATE_TIME = LocalDateTime.of(2024, 1, 1, 0, 0, 0);

    public DateTimeAnonymizer() {
        minDateTime = DEFAULT_MIN_LOCAL_DATE_TIME;
        maxDateTime = DEFAULT_MAX_LOCAL_DATE_TIME;
    }

    public DateTimeAnonymizer(LocalDateTime minDateTime, LocalDateTime maxDateTime) {
        this.minDateTime = minDateTime;
        this.maxDateTime = maxDateTime;
    }

    @Override
    public String anonymize(String input) {
        LocalDateTime originalDateTime = LocalDateTime.parse(input);
        initRandom(input.hashCode());
        LocalDateTime translatedDateTime = translateLocalDateTime(originalDateTime);
        DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return format.format(translatedDateTime);
    }

    private LocalDateTime translateLocalDateTime(LocalDateTime originalDateTime) {
        long secondsBetweenMinMax = Duration.between(minDateTime, maxDateTime).toSeconds();
        long secondsBetweenMinOriginal = Duration.between(minDateTime, originalDateTime).toSeconds();
        long randomSecond = (secondsBetweenMinOriginal + Math.abs(getRandom().nextInt())) % secondsBetweenMinMax;
        return this.minDateTime.plusSeconds(randomSecond);
    }
}
