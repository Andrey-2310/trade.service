package andreiredkouski.tradeservice.infrastructure.response;

import andreiredkouski.tradeservice.service.csv.CsvEntry;
import java.io.IOException;
import java.math.BigDecimal;
import org.apache.commons.csv.CSVPrinter;

public record TradeResponse(
        String date,
        String productName,
        String currency,
        BigDecimal price
) implements CsvEntry {

    @Override
    public void print(final CSVPrinter csvPrinter) throws IOException {
        csvPrinter.printRecord(date, productName, currency, price);
    }
}
