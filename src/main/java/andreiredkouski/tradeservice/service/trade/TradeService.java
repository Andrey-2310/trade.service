package andreiredkouski.tradeservice.service.trade;

import org.springframework.core.io.InputStreamSource;

/**
 * Represents a service for managing products.
 */
public interface TradeService {

    /**
     * Map incoming stream of Trades with productId into Trades with productName.
     * Writes the results into the Target Destination using writer.
     *
     * @param tradesSource incoming stream of trades.
     * @param writer       is used to determine the target destination to which Trades with productNames should be written.
     */
    void getTradesWithProductNames(InputStreamSource tradesSource, Appendable writer);
}
