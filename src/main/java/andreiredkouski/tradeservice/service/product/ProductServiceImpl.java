package andreiredkouski.tradeservice.service.product;

import andreiredkouski.tradeservice.service.infrastructure.domain.Product;
import andreiredkouski.tradeservice.service.infrastructure.repository.ProductRepository;
import andreiredkouski.tradeservice.service.csv.CsvParserService;
import andreiredkouski.tradeservice.service.infrastructure.metadata.ProductMetadata;
import jakarta.annotation.PostConstruct;
import java.util.Optional;
import java.util.function.Consumer;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Value("${redis.onstart.invalidate.products}")
    private Boolean invalidateProductsOnStart;
    @Value("${redis.onstart.update.products}")
    private Boolean updateProductsOnStart;
    @Value("${products.source.path}")
    private String productsSourcePath;

    private ProductRepository productRepository;
    private final CsvParserService csvParserService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(final ProductRepository productRepository,
                              final CsvParserService csvParserService) {
        this.productRepository = productRepository;
        this.csvParserService = csvParserService;
    }

    @Override
    public Optional<Product> findById(final Long id) {
        return productRepository.findById(id);
    }

    @PostConstruct
    public void refreshProducts() {
        if (invalidateProductsOnStart) {
            LOGGER.info("Invalidation of products started");
            productRepository.deleteAll();
            LOGGER.info("Invalidation of products finished");
        }
        if (updateProductsOnStart) {
            LOGGER.info("Updating products with the actual source file state started");
            updateProductsOnStart();
            LOGGER.info("Updating products with the actual source file state finished");
        }
    }

    private void updateProductsOnStart() {
        csvParserService.processCsvSource(
                new ClassPathResource(productsSourcePath),
                csvRecordConsumer
        );
    }

    final Consumer<CSVRecord> csvRecordConsumer = csvRecord -> {
        productRepository.save(convertToProduct(csvRecord));
    };

    private Product convertToProduct(CSVRecord csvRecord) {
        return new Product(
                Long.parseLong(csvRecord.get(ProductMetadata.PRODUCT_ID.value())),
                csvRecord.get(ProductMetadata.PRODUCT_NAME.value())
        );
    }
}
