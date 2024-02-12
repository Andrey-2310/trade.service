package andreiredkouski.tradeservice.infrastructure.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(value = BAD_REQUEST)
public class CsvProcessingException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Failed to parse CSV file: ";

    public CsvProcessingException(String message) {
        super(ERROR_MESSAGE + message);
    }
}
