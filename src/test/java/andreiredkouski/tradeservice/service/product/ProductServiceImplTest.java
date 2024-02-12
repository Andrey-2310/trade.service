package andreiredkouski.tradeservice.service.product;

import andreiredkouski.tradeservice.service.infrastructure.domain.Product;
import andreiredkouski.tradeservice.service.infrastructure.metadata.ProductMetadata;
import andreiredkouski.tradeservice.service.infrastructure.repository.ProductRepository;
import andreiredkouski.tradeservice.service.csv.CsvParserService;
import java.util.Optional;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    private final String invalidateProductsOnStart = "invalidateProductsOnStart";
    private final String updateProductsOnStart = "updateProductsOnStart";
    private final String productsSourcePath = "productsSourcePath";
    private final String testSourceLocation = "test-source.csv";

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CsvParserService csvParserService;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @InjectMocks
    private ProductServiceImpl productService;

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(productRepository, csvParserService);
    }

    @Test
    public void whenFindByValidIdShouldReturnProduct() {
        Long productId = 1L;
        Product expected = new Product(productId, "Test");
        when(productRepository.findById(productId))
                .thenReturn(Optional.of(expected));

        Optional<Product> actual = productService.findById(productId);

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
        verify(productRepository).findById(productId);
    }

    @Test
    public void whenFindByInvalidIdShouldReturnNull() {
        Long productId = 1L;
        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        Optional<Product> actual = productService.findById(productId);

        assertFalse(actual.isPresent());
        verify(productRepository).findById(productId);
    }

    @Test
    void whenRefreshProductsWithNoInvalidationAndUpdateFlagsShouldNotRefresh() {
        setField(productService, invalidateProductsOnStart, false);
        setField(productService, updateProductsOnStart, false);

        productService.refreshProducts();
    }

    @Test
    void whenRefreshProductsWithNoInvalidationFlagShouldOnlyUpdate() {
        setField(productService, invalidateProductsOnStart, false);
        setField(productService, updateProductsOnStart, true);
        setField(productService, productsSourcePath, testSourceLocation);
        doNothing().when(csvParserService).processCsvSource(any(), any());

        productService.refreshProducts();

        verify(csvParserService).processCsvSource(any(), any());
    }

    @Test
    void whenRefreshProductsWithOnlyInvalidationFlagShouldOnlyInvalidate() {
        setField(productService, invalidateProductsOnStart, true);
        setField(productService, updateProductsOnStart, false);
        doNothing().when(productRepository).deleteAll();

        productService.refreshProducts();

        verify(productRepository).deleteAll();
    }

    @Test
    void whenRefreshProductsWithFlagsShouldRefresh() {
        setField(productService, invalidateProductsOnStart, true);
        setField(productService, updateProductsOnStart, true);
        setField(productService, productsSourcePath, testSourceLocation);
        doNothing().when(productRepository).deleteAll();
        doNothing().when(csvParserService).processCsvSource(any(), any());

        productService.refreshProducts();

        verify(productRepository).deleteAll();
        verify(csvParserService).processCsvSource(any(), any());
    }

    @Test
    public void whenCallingCsvConsumerShouldSaveProduct() {
        Long productId = 1L;
        String productName = "Test name";
        CSVRecord csvRecord = mock(CSVRecord.class);
        when(csvRecord.get(ProductMetadata.PRODUCT_ID.value())).thenReturn(productId.toString());
        when(csvRecord.get(ProductMetadata.PRODUCT_NAME.value())).thenReturn(productName);
        when(productRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        productService.csvRecordConsumer.accept(csvRecord);

        verify(productRepository).save(productCaptor.capture());
        assertEquals(productId, productCaptor.getValue().id());
        assertEquals(productName, productCaptor.getValue().name());
    }
}
