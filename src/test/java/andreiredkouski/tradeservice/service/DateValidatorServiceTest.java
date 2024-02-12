package andreiredkouski.tradeservice.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class DateValidatorServiceTest {

    @InjectMocks
    private DateValidatorImpl dateValidator;

    @ParameterizedTest
    @ValueSource(strings = {"20241101", "19970507", "19740303", "20240229"})
    public void whenFormatIsValidShouldReturnTrue(final String date) {
        assertTrue(dateValidator.isValid(date, BASIC_ISO_DATE));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "2023/10/11", "23.01.2023", "20231301", "20240230", "20230229"})
    public void whenFormatIsInvalidShouldReturnFalse(final String date) {
        assertFalse(dateValidator.isValid(date, BASIC_ISO_DATE));
    }
}
