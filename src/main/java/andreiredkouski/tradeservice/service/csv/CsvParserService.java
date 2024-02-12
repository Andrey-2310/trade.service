package andreiredkouski.tradeservice.service.csv;

import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.InputStreamSource;

/**
 * Service for parsing incoming csv files with additional processing.
 * Additional processing is required in order to avoid the collection of csv records (might lead to memory issues).
 */
public interface CsvParserService {

    /**
     * Parses the incoming csv source and process each Record with recordProcessor.
     *
     * @param source          inputstream of data, typically from .csv file.
     * @param recordProcessor consumer that process the Record once it was retrieved from the stream.
     */
    void processCsvSource(InputStreamSource source,
                          Consumer<CSVRecord> recordProcessor
    );

    /**
     * Parses the incoming csv source and process each Record with recordProcessor.
     * Additionally, it prints the output into Target destination using writer.
     *
     * @param source          inputstream of data, typically from .csv file.
     * @param writer          is used to determine the target destination to which CsvEntry should be written.
     * @param recordProcessor consumer that process the Record once it was retrieved from the stream.
     * @param headers         represent the headers of the output .csv file.
     */
    void processCsvSourceWithTarget(InputStreamSource source,
                                    Appendable writer,
                                    Function<CSVRecord, CsvEntry> recordProcessor,
                                    Object[] headers
    );
}
