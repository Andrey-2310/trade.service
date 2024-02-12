package andreiredkouski.tradeservice.infrastructure.metadata;

public enum ProductMetadata {
    PRODUCT_ID("product_id"),
    PRODUCT_NAME("product_name");

    private final String value;

    ProductMetadata(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
