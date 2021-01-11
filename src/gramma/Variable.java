package gramma;

import environment.Environment;
import gramma.interfaces.Expression;
import gramma.interfaces.Value;

public class Variable implements Expression {

    private final String id;

    public Variable(String id) { this.id = id; }

    @Override
    public Value evaluate(Environment environment) {
        return null;
    }

    @Override
    public NodeType getType() {
        return null;
    }

    public String getId() { return id; }
}
