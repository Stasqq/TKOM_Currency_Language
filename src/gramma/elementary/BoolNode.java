package gramma.elementary;

import gramma.NodeType;
import gramma.interfaces.Value;

public class BoolNode implements Value {
    private boolean value;

    public BoolNode(boolean value) { this.value=value; }

    @Override
    public BoolNode isEqual(Value operand) {
        return null;
    }

    @Override
    public NodeType getType() { return NodeType.BOOL; }

    public boolean getValue() { return value; }
}
