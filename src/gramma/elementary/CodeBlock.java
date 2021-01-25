package gramma.elementary;

import environment.Environment;
import errors.EnvironmentException;
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

    public StatementOutput execute(Environment environment) throws EnvironmentException {
        for (Statement statement : statements) {

            if (statement.getType() == NodeType.RETURN_LINE) {
                return statement.execute(environment);
            }

            StatementOutput output = statement.execute(environment);
            if (output.isStatusReturn()) {
                return output;
            }
        }

        return new StatementOutput(ReturnStatus.BASIC);
    }

    public List<Statement> getStatements() { return statements; }
}
