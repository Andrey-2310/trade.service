package andreiredkouski.tradeservice.service;

import java.time.format.DateTimeFormatter;

public interface DateValidator {

    boolean isValid(final String dateStr, final DateTimeFormatter dateFormatter);
}
