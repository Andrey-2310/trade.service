package andreiredkouski.tradeservice.service.csv;

import andreiredkouski.tradeservice.service.infrastructure.exception.CsvProcessingException;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CsvParserServiceImplTest {

    private static final int AMOUNT_OF_ENTRIES_IN_TEST_SOURCE = 4;

    @Mock
    private Consumer<CSVRecord> mockConsumer;

    @Mock
    private Function<CSVRecord, CsvEntry> mockFunction;

    @InjectMocks
    private CsvParserServiceImpl csvParserService;

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(mockConsumer, mockFunction);
    }

    @Test
    public void whenHasValidSourceShouldProcessCsv() {
        doNothing().when(mockConsumer).accept(any());

        csvParserService.processCsvSource(new ClassPathResource("test-source.csv"), mockConsumer);

        verify(mockConsumer, times(AMOUNT_OF_ENTRIES_IN_TEST_SOURCE)).accept(any());
    }

    @Test
    public void whenHasInvalidSourceShouldThrowException() {
        assertThrows(
                CsvProcessingException.class,
                () -> csvParserService.processCsvSource(new ClassPathResource("invalid-test-source.csv"), mockConsumer)
        );
    }

    @Test
    public void whenHasValidSourceShouldProcessCsvWithTarget() throws IOException {
        CsvEntry csvEntry = mock(CsvEntry.class);
        Appendable appendable = mock(Appendable.class);
        when(mockFunction.apply(any())).thenReturn(csvEntry);
        doNothing().when(csvEntry).print(any());

        csvParserService.processCsvSourceWithTarget(
                new ClassPathResource("test-source.csv"),
                appendable,
                mockFunction,
                new Object[]{}
        );

        verify(csvEntry, times(AMOUNT_OF_ENTRIES_IN_TEST_SOURCE)).print(any());
        verify(mockFunction, times(AMOUNT_OF_ENTRIES_IN_TEST_SOURCE)).apply(any());
    }

    @Test
    public void whenHasInvalidSourceShouldThrowExceptionWithoutPrintingToTarget() {
        Appendable writer = mock(Appendable.class);
        assertThrows(
                CsvProcessingException.class,
                () -> csvParserService.processCsvSourceWithTarget(
                        new ClassPathResource("invalid-test-source.csv"),
                        writer,
                        mockFunction,
                        new Object[]{}
                )
        );
    }
}
