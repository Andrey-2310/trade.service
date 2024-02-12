package andreiredkouski.tradeservice.service.trade;

import andreiredkouski.tradeservice.service.infrastructure.domain.Product;
import andreiredkouski.tradeservice.service.infrastructure.metadata.TradeResponseMetadata;
import andreiredkouski.tradeservice.service.infrastructure.request.TradeRequest;
import andreiredkouski.tradeservice.service.infrastructure.response.TradeResponse;
import andreiredkouski.tradeservice.service.DateValidator;
import andreiredkouski.tradeservice.service.csv.CsvParserService;
import andreiredkouski.tradeservice.service.csv.CsvEntry;
import andreiredkouski.tradeservice.service.product.ProductService;
import andreiredkouski.tradeservice.service.infrastructure.metadata.TradeRequestMetadata;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;
import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;

@Service
public class TradeServiceImpl implements TradeService {

    private final CsvParserService csvParserService;
    private final ProductService productService;
    private DateValidator dateValidator;

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeServiceImpl.class);
    private static final DateTimeFormatter TRADE_DATE_FORMAT = BASIC_ISO_DATE;
    static final String DEFAULT_PRODUCT_NAME = "Missing Product Name";

    public TradeServiceImpl(final CsvParserService csvParserService,
                            final ProductService productService,
                            final DateValidator dateValidator
    ) {
        this.csvParserService = csvParserService;
        this.productService = productService;
        this.dateValidator = dateValidator;
    }

    @Override
    public void getTradesWithProductNames(final InputStreamSource tradesSource, final Appendable writer) {
        csvParserService.processCsvSourceWithTarget(
                tradesSource,
                writer,
                convertToTradesWithProductName,
                TradeResponseMetadata.headers()
        );
    }

    final Function<CSVRecord, CsvEntry> convertToTradesWithProductName = csvRecord -> {
        TradeRequest tradeRequest = getTradeRequest(csvRecord);
        if (dateValidator.isValid(tradeRequest.date(), TRADE_DATE_FORMAT)) {
            return convertToTradeResponse(tradeRequest);
        }
        return null;
    };

    private TradeRequest getTradeRequest(CSVRecord csvRecord) {
        return new TradeRequest(
                csvRecord.get(TradeRequestMetadata.DATE.value()),
                Long.parseLong(csvRecord.get(TradeRequestMetadata.PRODUCT_ID.value())),
                csvRecord.get(TradeRequestMetadata.CURRENCY.value()),
                new BigDecimal(csvRecord.get(TradeRequestMetadata.PRICE.value()))
        );
    }

    private TradeResponse convertToTradeResponse(final TradeRequest trade) {
        return new TradeResponse(
                trade.date(),
                getProductName(trade),
                trade.currency(),
                trade.price()
        );
    }

    private String getProductName(TradeRequest trade) {
        return productService.findById(trade.productId())
                .map(Product::name)
                .orElseGet(() -> {
                    LOGGER.warn(String.format("Product with id %d was not found, setting default value", trade.productId()));
                    return DEFAULT_PRODUCT_NAME;
                });
    }
}
