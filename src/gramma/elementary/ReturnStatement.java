package gramma.elementary;

import environment.Environment;
import errors.EnvironmentException;
import gramma.NodeType;
import gramma.interfaces.Expression;
import gramma.interfaces.Statement;

public class ReturnStatement implements Statement {

    private Expression expression;

    public ReturnStatement(Expression expression) { this.expression = expression; }

    @Override
    public StatementOutput execute(Environment environment) throws EnvironmentException {
        return new StatementOutput(ReturnStatus.RETURN, expression.evaluate(environment));
    }

    @Override
    public NodeType getType() { return NodeType.RETURN_LINE; }

    public Expression getExpression() { return expression; }
}
