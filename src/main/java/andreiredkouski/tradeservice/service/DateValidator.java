package andreiredkouski.tradeservice.service;

import java.time.format.DateTimeFormatter;

/**
 * DateValidator interface represents a utility for validating date strings.
 */
public interface DateValidator {

    /**
     * Checks if the given date string is valid according to the specified date format.
     *
     * @param dateStr       the date string to validate.
     * @param dateFormatter the {@link DateTimeFormatter} specifying the format to validate against.
     * @return {@code true} if the date string is valid according to the specified format, {@code false} otherwise.
     */
    boolean isValid(final String dateStr, final DateTimeFormatter dateFormatter);
}
