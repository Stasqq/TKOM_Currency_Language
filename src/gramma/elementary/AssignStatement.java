package gramma.elementary;

import environment.Environment;
import gramma.NodeType;
import gramma.interfaces.Expression;
import gramma.interfaces.Statement;

public class AssignStatement implements Statement {

    private String id;

    private Expression assign;

    public AssignStatement(String id, Expression assign) {
        this.id = id;
        this.assign = assign;
    }

    @Override
    public StatementOutput execute(Environment environment) {
        return null;
    }

    @Override
    public NodeType getType() { return NodeType.ASSIGN_LINE; }

    public String getId() { return id; }
}
