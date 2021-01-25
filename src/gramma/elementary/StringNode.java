package gramma.elementary;

import environment.Environment;
import errors.EnvironmentException;
import gramma.NodeType;
import gramma.interfaces.Expression;
import gramma.interfaces.Value;

public class StringNode implements Expression, Value {

    private final String value;

    public StringNode(String value) { this.value = value; }

    @Override
    public String toString() { return value; }

    @Override
    public Value evaluate(Environment environment) { return this; }

    @Override
    public BoolNode isEqual(Value operand) throws EnvironmentException {
        if (operand.getType() == NodeType.STRING) {
            return new BoolNode(value.equals(((StringNode) operand).value));
        }

        throw new EnvironmentException("Cannot compare: " + operand.getType() + " to " + getType());
    }

    @Override
    public NodeType getType() { return NodeType.STRING; }
}
