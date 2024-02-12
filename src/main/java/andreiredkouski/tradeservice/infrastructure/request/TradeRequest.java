package andreiredkouski.tradeservice.infrastructure.request;

import java.math.BigDecimal;

public record TradeRequest(
        String date,
        Long productId,
        String currency,
        BigDecimal price
) {
}
