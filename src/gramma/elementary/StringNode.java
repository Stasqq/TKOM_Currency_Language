package gramma.elementary;

import environment.Environment;
import gramma.NodeType;
import gramma.interfaces.Expression;
import gramma.interfaces.Value;

public class StringNode implements Expression, Value {

    private final String value;

    public StringNode(String value) { this.value = value; }

    @Override
    public Value evaluate(Environment environment) { return this; }

    @Override
    public BoolNode isEqual(Value operand)  {
        return null;
    }

    @Override
    public NodeType getType() { return NodeType.STRING; }
}
