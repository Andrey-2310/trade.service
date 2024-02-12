package andreiredkouski.tradeservice.service.csv;

import andreiredkouski.tradeservice.infrastructure.exception.CsvProcessingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;

@Service
public class CsvParserServiceImpl implements CsvParserService {

    private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT
            .builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .setTrim(true)
            .build();

    public void processCsvSource(final InputStreamSource source,
                                 final Consumer<CSVRecord> consumer
    ) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(source.getInputStream()));
             CSVParser csvParser = CSV_FORMAT.parse(fileReader)
        ) {
            for (CSVRecord csvRecord : csvParser) {
                consumer.accept(csvRecord);
            }
        } catch (IOException e) {
            throw new CsvProcessingException(e.getMessage());
        }
    }

    public void processCsvSourceWithTarget(final InputStreamSource source,
                                           final Appendable writer,
                                           final Function<CSVRecord, CsvEntry> function,
                                           final Object[] headers
    ) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(source.getInputStream()));
             CSVParser csvParser = CSV_FORMAT.parse(fileReader);
             CSVPrinter csvPrinter = CSV_FORMAT.print(writer);
        ) {
            csvPrinter.printRecord(headers);
            for (CSVRecord csvRecord : csvParser) {
                CsvEntry csvEntry = function.apply(csvRecord);
                if (csvEntry != null) {
                    csvEntry.print(csvPrinter);
                }
            }
        } catch (IOException e) {
            throw new CsvProcessingException(e.getMessage());
        }
    }
}
