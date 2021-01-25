package gramma.elementary;

import environment.Environment;
import errors.EnvironmentException;
import gramma.NodeType;
import gramma.interfaces.Arithmetic;
import gramma.interfaces.Expression;
import gramma.interfaces.Value;

import java.math.BigDecimal;

public class IntNode implements Expression, Arithmetic {

    private final int value;

    public IntNode(int value) { this.value = value; }

    @Override
    public String toString() { return String.valueOf(value); }


    @Override
    public Arithmetic add(Value addValue) throws EnvironmentException {
        switch (addValue.getType()) {
            case INT:
                return new IntNode(value + Value.getIntValue(addValue));
            case DOUBLE:
                return new DoubleNode(value + Value.getDoubleValue(addValue));
            default:
                throw new EnvironmentException("Cannot add " + addValue.getType() + " to " + getType());
        }
    }

    @Override
    public Arithmetic subtract(Value subValue) throws EnvironmentException {
        switch (subValue.getType()) {
            case INT:
                return new IntNode(value - Value.getIntValue(subValue));
            case DOUBLE:
                return new DoubleNode(value - Value.getDoubleValue(subValue));
            default:
                throw new EnvironmentException("Cannot subtract " + subValue.getType() + " from " + getType());
        }
    }

    @Override
    public Arithmetic multiply(Value mulValue) throws EnvironmentException {
        switch (mulValue.getType()) {
            case INT:
                return new IntNode(value * Value.getIntValue(mulValue));
            case DOUBLE:
                return new DoubleNode(value * Value.getDoubleValue(mulValue));
            case CURRENCY:
                return new Currency(((Currency) mulValue).getCurrencyType(), BigDecimal.valueOf(value * ((Currency) mulValue).getValue().doubleValue()));
            default:
                throw new EnvironmentException("Cannot multiply " + getType() + " by " + mulValue.getType());
        }
    }

    @Override
    public Arithmetic divide(Value divValue) throws EnvironmentException {
        if (Arithmetic.isZero(divValue)) {
            throw new EnvironmentException("Divide by 0!");
        }

        switch (divValue.getType()) {
            case INT:
                return new IntNode(value / Value.getIntValue(divValue));
            case DOUBLE:
                return new DoubleNode(value / Value.getDoubleValue(divValue));
            default:
                throw new EnvironmentException("Cannot divide " + getType() + " by " + divValue.getType());
        }
    }

    @Override
    public BoolNode isLess(Value comparisonValue) throws EnvironmentException {
        switch (comparisonValue.getType()) {
            case INT:
                return new BoolNode(value < Value.getIntValue(comparisonValue));
            case DOUBLE:
                return new BoolNode(value < Value.getDoubleValue(comparisonValue));
            default:
                throw new EnvironmentException("Cannot compare " + comparisonValue.getType() + " to " + getType());
        }
    }

    @Override
    public BoolNode isGreater(Value comparisonValue) throws EnvironmentException {
        switch (comparisonValue.getType()) {
            case INT:
                return new BoolNode(value > Value.getIntValue(comparisonValue));
            case DOUBLE:
                return new BoolNode(value > Value.getDoubleValue(comparisonValue));
            default:
                throw new EnvironmentException("Cannot compare " + comparisonValue.getType() + " to " + getType());
        }
    }

    @Override
    public BoolNode isLessOrEqual(Value comparisonValue) throws EnvironmentException {
        switch (comparisonValue.getType()) {
            case INT:
                return new BoolNode(value <= Value.getIntValue(comparisonValue));
            case DOUBLE:
                return new BoolNode(value <= Value.getDoubleValue(comparisonValue));
            default:
                throw new EnvironmentException("Cannot compare " + comparisonValue.getType() + " to " + getType());
        }
    }

    @Override
    public BoolNode isGreaterOrEqual(Value comparisonValue) throws EnvironmentException {
        switch (comparisonValue.getType()) {
            case INT:
                return new BoolNode(value >= Value.getIntValue(comparisonValue));
            case DOUBLE:
                return new BoolNode(value >= Value.getDoubleValue(comparisonValue));
            default:
                throw new EnvironmentException("Cannot compare " + comparisonValue.getType() + " to " + getType());
        }
    }

    @Override
    public Value evaluate(Environment environment) throws EnvironmentException {
        return this;
    }

    @Override
    public BoolNode isEqual(Value operand) throws EnvironmentException {
        switch (operand.getType()) {
            case INT:
                return new BoolNode(value == Value.getIntValue(operand));
            case DOUBLE:
                return new BoolNode(value == Value.getDoubleValue(operand));
            default:
                throw new EnvironmentException("Cannot compare " + operand.getType() + " to " + getType());
        }
    }

    @Override
    public NodeType getType() {
        return NodeType.INT;
    }

    public int getValue() { return value; }
}
