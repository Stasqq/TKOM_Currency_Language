package gramma.interfaces;
import errors.EnvironmentException;
import gramma.elementary.BoolNode;
import gramma.elementary.Currency;
import gramma.elementary.DoubleNode;
import gramma.elementary.IntNode;

import java.math.BigDecimal;

public interface Arithmetic extends Value {
    Arithmetic add(Value addValue) throws EnvironmentException;
    Arithmetic subtract(Value subValue) throws EnvironmentException;
    Arithmetic multiply(Value mulValue) throws EnvironmentException;
    Arithmetic divide(Value divValue) throws EnvironmentException;

    BoolNode isLess(Value comparisonValue) throws EnvironmentException;
    BoolNode isGreater(Value comparisonValue) throws EnvironmentException;
    BoolNode isLessOrEqual(Value comparisonValue) throws EnvironmentException;
    BoolNode isGreaterOrEqual(Value comparisonValue) throws EnvironmentException;

    static boolean isZero(Value value) throws EnvironmentException {
        switch (value.getType()) {
            case INT:
                return ((IntNode) value).getValue() == 0;
            case DOUBLE:
                return ((DoubleNode) value).getValue() == 0;
            case CURRENCY:
                return ((Currency) value).getValue().equals(BigDecimal.ZERO);
            default:
                throw new EnvironmentException("Wrong type while comparison to zero");
        }
    }
}
