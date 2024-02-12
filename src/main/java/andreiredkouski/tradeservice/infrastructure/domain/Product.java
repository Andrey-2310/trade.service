package andreiredkouski.tradeservice.infrastructure.domain;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("product")
public record Product(
        Long id,
        String name
) {
}


