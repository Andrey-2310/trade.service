package andreiredkouski.tradeservice.service.csv;

import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.InputStreamSource;

public interface CsvParserService {

    void processCsvSource(InputStreamSource source,
                          Consumer<CSVRecord> consumer
    );

    void processCsvSourceWithTarget(InputStreamSource source,
                                    Appendable writer,
                                    Function<CSVRecord, CsvEntry> function,
                                    Object[] headers
    );
}
