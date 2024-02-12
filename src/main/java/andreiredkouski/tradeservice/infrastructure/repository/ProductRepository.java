package andreiredkouski.tradeservice.infrastructure.repository;

import andreiredkouski.tradeservice.infrastructure.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
