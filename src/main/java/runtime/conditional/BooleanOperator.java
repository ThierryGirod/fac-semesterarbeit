package runtime.conditional;

public enum BooleanOperator {
    EQUAL("=="), NOT_EQUAL("!="), GREATER(">"), SMALLER("<"), GREATER_EQUAL(">="), SMALLER_EQUAL("<=");

    private final String str;

    private BooleanOperator(final String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
