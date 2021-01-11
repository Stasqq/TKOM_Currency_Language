package gramma.interfaces;
import gramma.elementary.BoolNode;

public interface Arithmetic extends Value {
    Arithmetic add(Value addValue);
    Arithmetic subtract(Value subValue);
    Arithmetic multiply(Value mulValue);
    Arithmetic divide(Value divValue);

    BoolNode isLess(Value comparisonValue);
    BoolNode isGreater(Value comparisonValue);
    BoolNode isLessOrEqual(Value comparisonValue);
    BoolNode isGreaterOrEqual(Value comparisonValue);

    static boolean isZero(Value value) { return false; }
}
