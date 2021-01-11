package gramma.elementary;

import environment.Environment;
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
    public Value evaluate(Environment environment) {
        return null;
    }

    @Override
    public StatementOutput execute(Environment environment) {
        return null;
    }

    @Override
    public NodeType getType() {
        return NodeType.FUNCTION_CALL;
    }

    public String getId() { return id; }
}
