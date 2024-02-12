package andreiredkouski.tradeservice.infrastructure.metadata;

public enum TradeResponseMetadata {
    DATE("date"),
    PRODUCT_NAME("product_name"),
    CURRENCY("currency"),
    PRICE("price");

    private final String value;

    TradeResponseMetadata(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static Object[] headers() {
        return new Object[]{DATE.value(), PRODUCT_NAME.value(), CURRENCY.value(), PRICE.value()};
    }
}
