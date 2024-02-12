package andreiredkouski.tradeservice.service.csv;

import java.io.IOException;
import org.apache.commons.csv.CSVPrinter;

/**
 * Represents the ability of the object to be printed into .csv file as a Record.
 */
public interface CsvEntry {

    /**
     * Prints the object into .csv file using given csvPrinter.
     * 
     * @param csvPrinter holds the information about output source, used for printing the Record.
     */
    void print(CSVPrinter csvPrinter) throws IOException;
}
