package runtime.arithmetic;

import java.util.Arrays;
import java.util.Optional;

public enum ArithmeticOperator {
    ADDITION("+"), SUBTRACTION("-"), MULTIPLICATION("*"), DIVISION("/");

    private final String str;

    private ArithmeticOperator(final String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

    public static ArithmeticOperator getByString(final String str) {
        Optional<ArithmeticOperator> result = Arrays.stream(ArithmeticOperator.values()).filter(o -> o.toString().equals(str))
                .findAny();
        if (result.isPresent()) {
            return result.get();
        }
        throw new IllegalArgumentException("MathOperator " + str + " is not available!");
    }
}
