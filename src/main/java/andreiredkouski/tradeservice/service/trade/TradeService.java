package andreiredkouski.tradeservice.service.trade;

import org.springframework.core.io.InputStreamSource;

public interface TradeService {

    void getTradesWithProductNames(InputStreamSource tradesSource, Appendable writer);
}
