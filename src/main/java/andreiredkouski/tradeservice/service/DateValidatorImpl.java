package andreiredkouski.tradeservice.service;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DateValidatorImpl implements DateValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateValidatorImpl.class);

    public boolean isValid(final String dateStr, final DateTimeFormatter dateFormatter) {
        try {
            LocalDate.parse(dateStr, dateFormatter);
        } catch (DateTimeParseException e) {
            LOGGER.warn(String.format("Date %s doesn't match the following format: %s", dateStr, dateFormatter.toString()));
            return false;
        }
        return true;
    }
}
