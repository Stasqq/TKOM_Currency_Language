package gramma;

import environment.Environment;
import errors.EnvironmentException;
import gramma.interfaces.Expression;
import gramma.interfaces.Value;

public class Variable implements Expression {

    private final String id;

    public Variable(String id) { this.id = id; }

    @Override
    public Value evaluate(Environment environment) throws EnvironmentException {
        return environment.getVariable(id);
    }

    @Override
    public NodeType getType() {
        return NodeType.VARIABLE;
    }

    public String getId() { return id; }
}
