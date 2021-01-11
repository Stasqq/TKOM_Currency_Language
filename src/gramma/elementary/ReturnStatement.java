package gramma.elementary;

import environment.Environment;
import gramma.NodeType;
import gramma.interfaces.Expression;
import gramma.interfaces.Statement;

public class ReturnStatement implements Statement {

    private Expression expression;

    public ReturnStatement(Expression expression) { this.expression = expression; }

    @Override
    public StatementOutput execute(Environment environment) {
        return null;
    }

    @Override
    public NodeType getType() { return NodeType.RETURN_LINE; }

    public Expression getExpression() { return expression; }
}
