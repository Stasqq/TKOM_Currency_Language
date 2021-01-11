package gramma.elementary;

import environment.Environment;
import gramma.NodeType;
import gramma.interfaces.Expression;
import gramma.interfaces.Value;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class ExpressionNode implements Expression {

    private final List<TokenType> operations;
    private final List<Expression> operands;

    public ExpressionNode() {
        this.operations = new ArrayList<>();
        this.operands = new ArrayList<>();
    }

    public void addOperation(TokenType operationType) {
        operations.add(operationType);
    }

    public void addOperand(Expression operand) {
        operands.add(operand);
    }

    @Override
    public NodeType getType() {
        return NodeType.EXPRESSION;
    }

    @Override
    public Value evaluate(Environment environment) {
        return null;
    }

    public List<TokenType> getOperations() { return operations; }
}
