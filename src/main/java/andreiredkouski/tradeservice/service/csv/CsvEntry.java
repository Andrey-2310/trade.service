package andreiredkouski.tradeservice.service.csv;

import java.io.IOException;
import org.apache.commons.csv.CSVPrinter;

public interface CsvEntry {

    void print(CSVPrinter csvPrinter) throws IOException;
}
