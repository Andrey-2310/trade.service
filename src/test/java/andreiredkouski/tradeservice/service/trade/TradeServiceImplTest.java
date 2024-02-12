package andreiredkouski.tradeservice.service.trade;

import andreiredkouski.tradeservice.service.infrastructure.domain.Product;
import andreiredkouski.tradeservice.service.infrastructure.metadata.TradeResponseMetadata;
import andreiredkouski.tradeservice.service.infrastructure.response.TradeResponse;
import andreiredkouski.tradeservice.service.DateValidator;
import andreiredkouski.tradeservice.service.csv.CsvEntry;
import andreiredkouski.tradeservice.service.csv.CsvParserService;
import andreiredkouski.tradeservice.service.product.ProductService;
import java.util.Optional;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamSource;
import static andreiredkouski.tradeservice.service.infrastructure.metadata.TradeRequestMetadata.CURRENCY;
import static andreiredkouski.tradeservice.service.infrastructure.metadata.TradeRequestMetadata.DATE;
import static andreiredkouski.tradeservice.service.infrastructure.metadata.TradeRequestMetadata.PRICE;
import static andreiredkouski.tradeservice.service.infrastructure.metadata.TradeRequestMetadata.PRODUCT_ID;
import static andreiredkouski.tradeservice.service.trade.TradeServiceImpl.DEFAULT_PRODUCT_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TradeServiceImplTest {

    private final String date = "20230101";
    private final String productId = "1";
    private final String productName = "Product Name";
    private final String currency = "EUR";
    private final String price = "10.1";

    @Mock
    private CsvParserService csvParserService;
    @Mock
    private ProductService productService;
    @Mock
    private DateValidator dateValidator;

    @InjectMocks
    private TradeServiceImpl tradeService;

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(csvParserService, productService, dateValidator);
    }

    @Test
    public void whenGetTradesWithProductNameShouldProcessCsvWithTarget() {
        InputStreamSource tradeSource = mock(InputStreamSource.class);
        Appendable writer = mock(Appendable.class);
        doNothing().when(csvParserService)
                .processCsvSourceWithTarget(any(), any(), any(), any());

        tradeService.getTradesWithProductNames(tradeSource, writer);

        verify(csvParserService).processCsvSourceWithTarget(
                tradeSource,
                writer,
                tradeService.convertToTradesWithProductName,
                TradeResponseMetadata.headers()
        );
    }

    @Test
    public void whenConvertingToTradesWithInvalidDateShouldReturnNull() {
        CSVRecord tradeRecord = prepareTradeRecord();
        when(dateValidator.isValid(eq(date), any())).thenReturn(false);

        CsvEntry actual = tradeService.convertToTradesWithProductName.apply(tradeRecord);

        assertNull(actual);
        verify(dateValidator).isValid(eq(date), any());
    }

    @Test
    public void whenConvertingToTradesWithValidDateShouldNotReturnNull() {
        CSVRecord tradeRecord = prepareTradeRecord();
        when(dateValidator.isValid(eq(date), any())).thenReturn(true);
        when(productService.findById(Long.parseLong(productId)))
                .thenReturn(Optional.of(new Product(Long.parseLong(productId), productName)));

        CsvEntry actual = tradeService.convertToTradesWithProductName.apply(tradeRecord);

        assertNotNull(actual);
        TradeResponse tradeResponse = (TradeResponse) actual;
        assertEquals(tradeResponse.date(), date);
        assertEquals(tradeResponse.productName(), productName);
        assertEquals(tradeResponse.currency(), currency);
        assertEquals(tradeResponse.price().toString(), price);
        verify(dateValidator).isValid(eq(date), any());
        verify(productService).findById(Long.parseLong(productId));
    }

    @Test
    public void whenConvertingToTradesWithInvalidProductShouldReturnDefaultProductName() {
        CSVRecord tradeRecord = prepareTradeRecord();
        when(dateValidator.isValid(eq(date), any())).thenReturn(true);
        when(productService.findById(Long.parseLong(productId)))
                .thenReturn(Optional.empty());

        CsvEntry actual = tradeService.convertToTradesWithProductName.apply(tradeRecord);

        assertNotNull(actual);
        TradeResponse tradeResponse = (TradeResponse) actual;
        assertEquals(tradeResponse.productName(), DEFAULT_PRODUCT_NAME);
        verify(dateValidator).isValid(eq(date), any());
        verify(productService).findById(Long.parseLong(productId));
    }

    private CSVRecord prepareTradeRecord() {
        CSVRecord csvRecord = mock(CSVRecord.class);
        when(csvRecord.get(DATE.value())).thenReturn(date);
        when(csvRecord.get(PRODUCT_ID.value())).thenReturn(productId);
        when(csvRecord.get(CURRENCY.value())).thenReturn(currency);
        when(csvRecord.get(PRICE.value())).thenReturn(price);
        return csvRecord;
    }


}
