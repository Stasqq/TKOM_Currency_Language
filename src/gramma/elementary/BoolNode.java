package gramma.elementary;

import environment.Environment;
import errors.EnvironmentException;
import gramma.NodeType;
import gramma.interfaces.Arithmetic;
import gramma.interfaces.Expression;
import gramma.interfaces.Value;

import java.math.BigDecimal;

public class BoolNode extends Condition implements Expression, Value, Arithmetic {
    private boolean value;

    public BoolNode(boolean value) { this.value=value; }

    public BoolNode(Value fromValue) {
        switch (fromValue.getType()) {
            case CURRENCY:
                this.value = !((Currency) fromValue).getValue().equals(BigDecimal.ZERO);
                break;
            case DOUBLE:
                this.value = ((DoubleNode) fromValue).getValue() != 0;
                break;
            case INT:
                this.value = ((IntNode) fromValue).getValue() != 0;
                break;
            case BOOL:
                this.value = ((BoolNode) fromValue).getValue();
                break;
        }
    }

    @Override
    public BoolNode isEqual(Value operand) throws EnvironmentException {
        if(!Value.isBool(operand))
            throw new EnvironmentException("Can't compare different types: "+operand+" with "+getType());

        return new BoolNode(value == Value.getBoolValue(operand));
    }

    public BoolNode or(Value operand) throws EnvironmentException {
        if(!Value.isBool(operand))
            throw new EnvironmentException("Can't compare different types: "+operand+" with "+getType());

        return new BoolNode(value || Value.getBoolValue(operand));
    }

    public BoolNode and(Value operand) throws EnvironmentException {
        if(!Value.isBool(operand))
            throw new EnvironmentException("Can't compare different types: "+operand+" with "+getType());

        return new BoolNode(value && Value.getBoolValue(operand));
    }

    @Override
    public NodeType getType() { return NodeType.BOOL; }

    public boolean getValue() { return value; }

    @Override
    public Value evaluate(Environment environment) throws EnvironmentException {
        return this;
    }

    @Override
    public Arithmetic add(Value addValue) throws EnvironmentException {
        throw new EnvironmentException("Cannot add " + addValue.getType() + " to " + getType());
    }

    @Override
    public Arithmetic subtract(Value subValue) throws EnvironmentException {
        throw new EnvironmentException("Cannot subtract " + subValue.getType() + " from " + getType());
    }

    @Override
    public Arithmetic multiply(Value mulValue) throws EnvironmentException {
        throw new EnvironmentException("Cannot multiply " + getType() + " by " + mulValue.getType());
    }

    @Override
    public Arithmetic divide(Value divValue) throws EnvironmentException {
        throw new EnvironmentException("Cannot divide " + getType() + " by " + divValue.getType());
    }

    @Override
    public BoolNode isLess(Value comparisonValue) throws EnvironmentException {
        throw new EnvironmentException("Cannot compare " + comparisonValue.getType() + " to " + getType());
    }

    @Override
    public BoolNode isGreater(Value comparisonValue) throws EnvironmentException {
        throw new EnvironmentException("Cannot compare " + comparisonValue.getType() + " to " + getType());
    }

    @Override
    public BoolNode isLessOrEqual(Value comparisonValue) throws EnvironmentException {
        throw new EnvironmentException("Cannot compare " + comparisonValue.getType() + " to " + getType());
    }

    @Override
    public BoolNode isGreaterOrEqual(Value comparisonValue) throws EnvironmentException {
        throw new EnvironmentException("Cannot compare " + comparisonValue.getType() + " to " + getType());
    }
}
