package andreiredkouski.tradeservice.service.product;

import andreiredkouski.tradeservice.service.infrastructure.domain.Product;
import java.util.Optional;

public interface ProductService {

    Optional<Product> findById(Long id);
}
