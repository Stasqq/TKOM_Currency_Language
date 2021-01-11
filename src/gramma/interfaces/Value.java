package gramma.interfaces;

import gramma.NodeType;
import gramma.elementary.BoolNode;
import gramma.elementary.Currency;
import gramma.elementary.DoubleNode;
import gramma.elementary.IntNode;

import java.math.BigDecimal;

public interface Value extends Node {
    BoolNode isEqual(Value operand);

    static int getIntValue(Value operand) {
        return ((IntNode) operand).getValue();
    }

    static double getDoubleValue(Value operand) {
        return ((DoubleNode) operand).getValue();
    }

    static BigDecimal getCurrencyValue(Value operand) {
        return ((Currency) operand).getValue();
    }

    static boolean getBoolValue(Value operand) {
        return ((BoolNode) operand).getValue();
    }

    static boolean isInt(Value operand) {
        return operand.getType() == NodeType.INT;
    }

    static boolean isDouble(Value operand) {
        return operand.getType() == NodeType.DOUBLE;
    }

    static boolean isCurrency(Value operand) {
        return operand.getType() == NodeType.CURRENCY;
    }

    static boolean isBool(Value operand) {
        return operand.getType() == NodeType.BOOL;
    }
}
