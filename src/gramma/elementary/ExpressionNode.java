package gramma.elementary;

import environment.Environment;
import errors.EnvironmentException;
import gramma.NodeType;
import gramma.interfaces.Arithmetic;
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
    public Value evaluate(Environment environment) throws EnvironmentException {
        int currentIndex = 0;
        Arithmetic leftOperand = (Arithmetic) operands.get(currentIndex).evaluate(environment);

        for (TokenType operation : operations) {
            currentIndex++;
            Arithmetic rightOperand = (Arithmetic) operands.get(currentIndex).evaluate(environment);

            leftOperand = executeOperation(operation, leftOperand, rightOperand);
        }

        return leftOperand;
    }

    private Arithmetic executeOperation(TokenType operation, Arithmetic leftOperand, Arithmetic rightOperand) throws EnvironmentException {
        switch (operation) {
            case DIVIDE:
                return leftOperand.divide(rightOperand);
            case MULTIPLY:
                return leftOperand.multiply(rightOperand);
            case PLUS:
                return leftOperand.add(rightOperand);
            case MINUS:
                return leftOperand.subtract(rightOperand);
            default:
                throw new EnvironmentException("Invalid operation: " + operation);
        }
    }

    public List<TokenType> getOperations() { return operations; }
}
