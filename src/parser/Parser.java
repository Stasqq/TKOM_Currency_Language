package parser;

import errors.TokenException;
import errors.WrongTokenTypeException;
import gramma.Variable;
import gramma.elementary.*;
import gramma.interfaces.Expression;
import gramma.interfaces.Statement;
import lexer.Lexer;
import token.Token;
import token.TokenAttributes;
import token.TokenType;

import java.io.IOException;
import java.util.List;

public class Parser {

    private Lexer lexer;

    private boolean buffer = false;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Token getToken() {
        return lexer.getToken();
    }

    Token getNextToken(TokenType expectedType) throws IOException, TokenException, WrongTokenTypeException {
        if (progress().getTokenType() != expectedType) {
            throw new WrongTokenTypeException(getToken().getLineNumber(), getToken().getPositionInLine(),
                    expectedType, getToken().getTokenType());
        }
        return getToken();
    }

    Token getNextToken(List<TokenType> expectedTypes) throws IOException, TokenException, WrongTokenTypeException {
        if (!expectedTypes.contains(progress().getTokenType())) {
            throw new WrongTokenTypeException(getToken().getLineNumber(), getToken().getPositionInLine(),
                    expectedTypes, getToken().getTokenType());
        }
        return getToken();
    }

    boolean getOptionalToken(TokenType type) throws IOException, TokenException {
        if (buffer) {
            if (getToken().getTokenType() == type) {
                buffer = false;
                return true;
            } else
                return false;
        } else {
            if (progress().getTokenType() == type) {
                return true;
            } else {
                buffer = true;
                return false;
            }
        }
    }

    boolean getOptionalToken(List<TokenType> types) throws IOException, TokenException {
        if (buffer) {
            if (types.contains(getToken().getTokenType())) {
                buffer = false;
                return true;
            } else
                return false;
        } else {
            if (types.contains(progress().getTokenType())) {
                return true;
            } else {
                buffer = true;
                return false;
            }
        }
    }

    Statement assignOrFunctionCallStatement() throws IOException, TokenException, WrongTokenTypeException {
        String id = getToken().getStringValue();

        if (getOptionalToken(TokenType.ASSIGMENT)) {
            return assignStatement(id);
        } else if (getOptionalToken(TokenType.OPEN_ROUND_BRACKET)) {
            FunctionCall functionCall = functionCall(id);
            getNextToken(TokenType.SEMICOLON);
            return functionCall;
        }

        throw new WrongTokenTypeException(getToken().getLineNumber(), getToken().getPositionInLine(),
                TokenAttributes.lineTypes, getToken().getTokenType());
    }

    AssignStatement assignStatement(String id) throws WrongTokenTypeException, IOException, TokenException {
        AssignStatement statement = new AssignStatement(id, expression());

        getNextToken(TokenType.SEMICOLON);

        return statement;
    }

    PrintStatement print() throws IOException, TokenException, WrongTokenTypeException {
        PrintStatement printStatement = new PrintStatement();

        getNextToken(TokenType.OPEN_ROUND_BRACKET);

        if (!getOptionalToken(TokenType.CLOSE_ROUND_BRACKET)) {
            printArguments(printStatement);
        }

        getNextToken(TokenType.SEMICOLON);

        return printStatement;
    }

    void printArguments(PrintStatement printStatement) throws IOException, TokenException, WrongTokenTypeException {
        do {
            printStatement.addArgument(expressionOrString());
        } while (getOptionalToken(TokenType.COMMA));

        getNextToken(TokenType.CLOSE_ROUND_BRACKET);
    }

    Expression expressionOrString() throws IOException, TokenException, WrongTokenTypeException {
        if (getOptionalToken(TokenType.STRING)) {
            return new StringNode(getToken().getStringValue());
        } else {
            return expression();
        }
    }

    AmountStatement amount() throws WrongTokenTypeException, IOException, TokenException {
        AmountStatement amountStatement = new AmountStatement();

        getNextToken(TokenType.OPEN_ROUND_BRACKET);

        if (!getOptionalToken(TokenType.CLOSE_ROUND_BRACKET)) {
            amountStatement.addCurrency(expression());
        }

        getNextToken(TokenType.SEMICOLON);

        return amountStatement;
    }

    DeclarationStatement declarationLine() throws WrongTokenTypeException, IOException, TokenException {
        DeclarationStatement declarationStatement = new DeclarationStatement(getToken().getStringValue(),
                getNextToken(TokenType.ID).getStringValue());

        if (getOptionalToken(TokenType.ASSIGMENT)) {
            declarationStatement.setAssign(expression());
        }

        getNextToken(TokenType.SEMICOLON);

        return declarationStatement;
    }

    IfStatement ifStatement() throws WrongTokenTypeException, IOException, TokenException {
        getNextToken(TokenType.OPEN_ROUND_BRACKET);
        IfStatement ifStatement = new IfStatement(condition());
        getNextToken(TokenType.CLOSE_ROUND_BRACKET);

        ifStatement.setIfTrueBlock(singleStatementOrBlock());

        if (getOptionalToken(TokenType.ELSE))
            ifStatement.setIfFalseBlock(singleStatementOrBlock());

        return ifStatement;
    }

    WhileStatement whileStatement() throws WrongTokenTypeException, IOException, TokenException {
        WhileStatement whileLine = new WhileStatement();

        getNextToken(TokenType.OPEN_ROUND_BRACKET);
        whileLine.setWhileCondition(condition());
        getNextToken(TokenType.CLOSE_ROUND_BRACKET);

        whileLine.setWhileBlock(singleStatementOrBlock());

        return whileLine;
    }

    public Condition condition() throws IOException, TokenException, WrongTokenTypeException {
        Condition condition = new Condition();
        condition.addOperand(andCondition());

        while (getOptionalToken(TokenType.OR)) {
            condition.setOperator(TokenType.OR);
            condition.addOperand(andCondition());
        }
        return condition;
    }

    Condition andCondition() throws IOException, TokenException, WrongTokenTypeException {
        Condition condition = new Condition();

        condition.addOperand(equalityCondition());

        while (getOptionalToken(TokenType.AND)) {
            condition.setOperator(TokenType.AND);
            condition.addOperand(equalityCondition());
        }
        return condition;
    }

    public Condition equalityCondition() throws IOException, TokenException, WrongTokenTypeException {
        Condition condition = new Condition();

        condition.addOperand(relationalCondition());

        if (getOptionalToken(TokenAttributes.equalityOperators)) {
            condition.setOperator(getToken().getTokenType());
            condition.addOperand(relationalCondition());
        }

        return condition;
    }

    public Condition relationalCondition() throws IOException, TokenException, WrongTokenTypeException {
        Condition condition = new Condition();

        condition.addOperand(baseCondition());

        if (getOptionalToken(TokenAttributes.relationOperators)) {
            condition.setOperator(getToken().getTokenType());
            condition.addOperand(baseCondition());
        }

        return condition;
    }

    public Condition baseCondition() throws IOException, TokenException, WrongTokenTypeException {
        Condition condition;
        boolean negation = false;

        if (getOptionalToken(TokenType.NOT)) {
            negation = true;
        }

        if (getOptionalToken(TokenType.OPEN_ROUND_BRACKET)) {
            condition = condition();
            getNextToken(TokenType.CLOSE_ROUND_BRACKET);
        } else {
            condition = new Condition();
            condition.addOperand(expression());
        }

        condition.setIsNegated(negation);
        return condition;
    }

    CodeBlock singleStatementOrBlock() throws IOException, TokenException, WrongTokenTypeException {
        if (getOptionalToken(TokenType.OPEN_BUCKLE)) {
            return codeBlock();
        } else {
            getOptionalToken(TokenAttributes.lineTypes);
            return singleStatementAsBlock();
        }
    }

    CodeBlock singleStatementAsBlock() throws WrongTokenTypeException, IOException, TokenException {
        CodeBlock block = new CodeBlock();
        block.addStatement(statement());
        return block;
    }

    public CodeBlock codeBlock() throws IOException, TokenException, WrongTokenTypeException {
        CodeBlock codeBlock = new CodeBlock();

        while (getOptionalToken(TokenAttributes.lineTypes)) {
            codeBlock.addStatement(statement());
        }

        getNextToken(TokenType.CLOSE_BUCKLE);
        return codeBlock;
    }

    public Statement statement() throws WrongTokenTypeException, IOException, TokenException {
        switch (getToken().getTokenType()) {
            case PRINT:
                return print();
            case AMOUNT:
                return amount();
            case ID:
                return assignOrFunctionCallStatement();
            case IF:
                return ifStatement();
            case WHILE:
                return whileStatement();
            case RETURN:
                return returnStatement();
            case INT:
            case DOUBLE:
            case CURRENCY:
                return declarationLine();
            default:
                throw new WrongTokenTypeException(getToken().getLineNumber(), getToken().getPositionInLine(),
                        TokenAttributes.lineTypes, getToken().getTokenType());
        }
    }

    ReturnStatement returnStatement() throws WrongTokenTypeException, IOException, TokenException {
        ReturnStatement returnStatement = new ReturnStatement(expression());

        getNextToken(TokenType.SEMICOLON);

        return returnStatement;
    }

    public Function function() throws WrongTokenTypeException, IOException, TokenException {
        Function function = new Function(getToken().getStringValue(), getNextToken(TokenType.ID).getStringValue());

        getNextToken(TokenType.OPEN_ROUND_BRACKET);

        if (getOptionalToken(TokenAttributes.valueTypes)) {
            parameterList(function);
        }

        getNextToken(TokenType.CLOSE_ROUND_BRACKET);
        getNextToken(TokenType.OPEN_BUCKLE);

        function.setCodeBlock(codeBlock());

        return function;
    }

    private void parameterList(Function function) throws WrongTokenTypeException, IOException, TokenException {
        do {
            Signature param = parameter();
            checkParameter(function.getParameters(), param);
            function.addParameter(param);
        } while (getOptionalToken(TokenType.COMMA) && getNextToken(TokenAttributes.valueTypes) != null);
    }

    Signature parameter() throws IOException, WrongTokenTypeException, TokenException {
        return new Signature(getToken().getStringValue(), getNextToken(TokenType.ID).getStringValue()
        );
    }

    void checkParameter(List<Signature> parameters, Signature param) throws WrongTokenTypeException {
        for (Signature signature : parameters) {
            if (signature.getId().equals(param.getId())) {
                throw new WrongTokenTypeException("Parameter " + param.getId() + " is already defined in scope.");
            }
        }
    }

    public Program program() throws IOException, TokenException, WrongTokenTypeException {
        Program program = new Program();

        while (getOptionalToken(TokenAttributes.valueTypes)) {
            program.addFunction(function());
        }

        return program;
    }

    Expression literal() throws WrongTokenTypeException, IOException, TokenException {
        Expression literal;
        int sign = getLiteralSign();
        if (getToken().getStringValue().contains("."))
            literal = new DoubleNode(getToken().getBDecimalValue().doubleValue() * sign);
        else
            literal = new IntNode(getToken().getBDecimalValue().intValue() * sign);
        return literal;
    }

    private int getLiteralSign() throws WrongTokenTypeException, IOException, TokenException {
        if (getToken().getTokenType() == TokenType.MINUS) {
            getNextToken(TokenType.NUMBER);
            return -1;
        }
        return 1;
    }

    public Expression priorityOperation() throws WrongTokenTypeException, IOException, TokenException {
        ExpressionNode expressionNode = new ExpressionNode();

        expressionNode.addOperand(baseExpression());

        while (getOptionalToken(TokenAttributes.highPriorityOperators)) {
            expressionNode.addOperation(getToken().getTokenType());
            expressionNode.addOperand(baseExpression());
        }

        return expressionNode;
    }

    public Expression expression() throws IOException, TokenException, WrongTokenTypeException {
        ExpressionNode expressionNode = new ExpressionNode();

        expressionNode.addOperand(priorityOperation());

        while (getOptionalToken(TokenAttributes.lowPriorityOperators)) {
            expressionNode.addOperation(getToken().getTokenType());
            expressionNode.addOperand(priorityOperation());
        }

        return expressionNode;
    }

    FunctionCall functionCall(String id) throws IOException, TokenException, WrongTokenTypeException {
        FunctionCall functionCall = new FunctionCall(id);

        if (!getOptionalToken(TokenType.CLOSE_ROUND_BRACKET)) {
            arguments(functionCall);
        }

        return functionCall;
    }

    void arguments(FunctionCall functionCall) throws IOException, TokenException, WrongTokenTypeException {
        do {
            functionCall.addArgument(expression());
        } while (getOptionalToken(TokenType.COMMA));
        getNextToken(TokenType.CLOSE_ROUND_BRACKET);
    }

    public Expression baseExpression() throws IOException, TokenException, WrongTokenTypeException {
        switch (progress().getTokenType()) {
            case MINUS:
            case NUMBER:
                return literal();
            case OPEN_ROUND_BRACKET: {
                Expression expression = expression();
                getNextToken(TokenType.CLOSE_ROUND_BRACKET);
                return expression;
            }
            case ID: {
                String id = getToken().getStringValue();
                if (getOptionalToken(TokenType.OPEN_ROUND_BRACKET))
                    return functionCall(id);
                else
                    return new Variable(id);
            }
        }

        throw new WrongTokenTypeException("Expression wrong token type!");
    }

    public Token progress() throws IOException, TokenException {
        if (buffer) {
            buffer = false;
            return lexer.getToken();
        }
        return lexer.getNextToken();
    }
}
