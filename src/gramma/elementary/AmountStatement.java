package gramma.elementary;

import environment.Environment;
import gramma.NodeType;
import gramma.interfaces.Expression;
import gramma.interfaces.Node;
import gramma.interfaces.Statement;

public class AmountStatement implements Node, Statement {

    private Expression expression;

    public void addCurrency(Expression expression){
        this.expression = expression;
    }

    @Override
    public StatementOutput execute(Environment environment) {
        return null;
    }

    @Override
    public NodeType getType() { return NodeType.AMOUNT_LINE; }
}
