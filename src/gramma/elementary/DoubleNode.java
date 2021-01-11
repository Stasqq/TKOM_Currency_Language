package gramma.elementary;

import environment.Environment;
import gramma.NodeType;
import gramma.interfaces.Arithmetic;
import gramma.interfaces.Expression;
import gramma.interfaces.Value;

public class DoubleNode implements Expression, Arithmetic {

    private final double value;

    public DoubleNode(double value) { this.value = value; }

    @Override
    public NodeType getType() { return NodeType.DOUBLE; }

    @Override
    public Arithmetic add(Value addValue) {
        return null;
    }

    @Override
    public Arithmetic subtract(Value subValue) {
        return null;
    }

    @Override
    public Arithmetic multiply(Value mulValue) {
        return null;
    }

    @Override
    public Arithmetic divide(Value divValue) {
        return null;
    }

    @Override
    public BoolNode isLess(Value comparisonValue) {
        return null;
    }

    @Override
    public BoolNode isGreater(Value comparisonValue) {
        return null;
    }

    @Override
    public BoolNode isLessOrEqual(Value comparisonValue) {
        return null;
    }

    @Override
    public BoolNode isGreaterOrEqual(Value comparisonValue) {
        return null;
    }

    @Override
    public Value evaluate(Environment environment) {
        return this;
    }

    @Override
    public BoolNode isEqual(Value operand) {
        return null;
    }

    public double getValue() { return value; }
}
