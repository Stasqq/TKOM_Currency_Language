package gramma.elementary;

import environment.Environment;
import gramma.NodeType;
import gramma.interfaces.Expression;
import gramma.interfaces.Node;
import gramma.interfaces.Statement;

import java.util.ArrayList;
import java.util.List;

public class PrintStatement implements Node, Statement {

    List<Expression> arguments;

    public PrintStatement() {
        this.arguments = new ArrayList<>();
    }

    public void addArgument(Expression argument) {
        arguments.add(argument);
    }

    @Override
    public StatementOutput execute(Environment environment) {
        return null;
    }

    @Override
    public NodeType getType() { return NodeType.PRINT_LINE; }

    public List<Expression> getArguments() { return arguments; }
}
