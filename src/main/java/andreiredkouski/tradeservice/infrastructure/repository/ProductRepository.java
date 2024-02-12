package andreiredkouski.tradeservice.service.infrastructure.repository;

import andreiredkouski.tradeservice.service.infrastructure.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
