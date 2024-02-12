package andreiredkouski.tradeservice.infrastructure.metadata;

public enum TradeRequestMetadata {
    DATE("date"),
    PRODUCT_ID("product_id"),
    CURRENCY("currency"),
    PRICE("price");

    private final String value;

    TradeRequestMetadata(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
