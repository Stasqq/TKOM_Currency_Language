package gramma.elementary;

import environment.Environment;
import errors.EnvironmentException;
import gramma.NodeType;
import gramma.interfaces.Expression;
import gramma.interfaces.Statement;
import gramma.interfaces.Value;

import java.util.ArrayList;
import java.util.List;

public class FunctionCall implements Statement, Expression {

    private String id;
    private List<Expression> arguments;

    public FunctionCall(String id) {
        this.id = id;
        this.arguments = new ArrayList<>();
    }

    public void addArgument(Expression expression) { arguments.add(expression); }

    @Override
    public Value evaluate(Environment environment) throws EnvironmentException {
        Function function = environment.getFunction(id);
        if (function == null) {
            throw new EnvironmentException("Undefined reference to function: " + id);
        }
        return function.execute(environment, arguments);
    }

    @Override
    public StatementOutput execute(Environment environment) throws EnvironmentException {
        evaluate(environment);
        return new StatementOutput(ReturnStatus.BASIC);
    }

    @Override
    public NodeType getType() {
        return NodeType.FUNCTION_CALL;
    }

    public String getId() { return id; }
}
