package andreiredkouski.tradeservice.service.infrastructure.metadata;

public enum TradeRequestMetadata {
    DATE("date"),
    PRODUCT_ID("product_id"),
    CURRENCY("currency"),
    PRICE("price");

    private final String value;

    TradeRequestMetadata(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
