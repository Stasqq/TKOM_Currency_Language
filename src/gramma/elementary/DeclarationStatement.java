package gramma.elementary;

import environment.Environment;
import gramma.NodeType;
import gramma.interfaces.Expression;
import gramma.interfaces.Statement;

public class DeclarationStatement extends Signature implements Statement {

    private Expression assign;

    public DeclarationStatement(String returnType, String id) {
        super(returnType, id);
    }

    @Override
    public StatementOutput execute(Environment environment) {
        return null;
    }

    public void setAssign(Expression expression) { this.assign = expression; }

    @Override
    public NodeType getType() { return NodeType.DECLARATION_LINE; }
}
