package gramma.elementary;

import environment.Environment;
import gramma.NodeType;
import gramma.interfaces.Node;
import gramma.interfaces.Statement;

import java.util.ArrayList;
import java.util.List;

public class CodeBlock implements Node {
    private final List<Statement> statements;

    public CodeBlock() {
        statements = new ArrayList<>();
    }

    public void addStatement(Statement statement) { statements.add(statement); }

    @Override
    public NodeType getType() {
        return NodeType.CODE_BLOCK;
    }

    public StatementOutput execute(Environment environment)  {
        return null;
    }

    public List<Statement> getStatements() { return statements; }
}
