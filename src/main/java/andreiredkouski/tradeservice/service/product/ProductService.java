package andreiredkouski.tradeservice.service.product;

import andreiredkouski.tradeservice.infrastructure.domain.Product;
import java.util.Optional;

/**
 * Represents a service for managing products.
 */
public interface ProductService {

    /**
     * Retrieves a Product by its ID.
     *
     * @param id the ID of the Product to retrieve
     * @return an {@link Optional} containing the Product if found, or {@link Optional#empty()} if no Product exists with the given ID
     */
    Optional<Product> findById(Long id);
}
