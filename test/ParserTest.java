import errors.TokenException;
import errors.WrongTokenTypeException;
import gramma.Variable;
import gramma.elementary.*;
import inputReaders.CurrenciesScanner;
import lexer.Lexer;
import org.junit.Test;
import parser.Parser;
import token.TokenType;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParserTest {
    private Parser parser;

    private void initParser(String input){
        CurrenciesScanner scanner = new CurrenciesScanner("inputs/currencies.txt");
        List<String> currencies = scanner.readCurrenciesNameList();
        Lexer lexer = new Lexer(new StringReader(input), currencies);
        parser = new Parser(lexer);
    }

    @Test (expected = WrongTokenTypeException.class)
    public void parseInvalidToken() throws IOException, TokenException, WrongTokenTypeException {
        String in = "1";
        initParser(in);

        parser.progress();
        parser.statement();
    }

    @Test
    public void parseDoubleExpression() throws WrongTokenTypeException, IOException, TokenException {
        DoubleNode expected = new DoubleNode(1.0);
        String in = "1.0";
        initParser(in);
        DoubleNode given = (DoubleNode) parser.baseExpression();

        assertEquals((Double) given.getValue(), (Double) expected.getValue());
    }

    @Test
    public void parseIntExpression() throws WrongTokenTypeException, IOException, TokenException {
        IntNode expected = new IntNode(1);
        String in = "1";
        initParser(in);
        IntNode given = (IntNode) parser.baseExpression();

        assertEquals((Integer) given.getValue(), (Integer) expected.getValue());
    }

    @Test
    public void parseMinusIntExpression() throws WrongTokenTypeException, IOException, TokenException {
        IntNode expected = new IntNode(-1);
        String in = "-1";
        initParser(in);
        IntNode given = (IntNode) parser.baseExpression();

        assertEquals((Integer) given.getValue(), (Integer) expected.getValue());
    }

    @Test
    public void parseComplexOperation() throws WrongTokenTypeException, IOException, TokenException {
        ExpressionNode expected = new ExpressionNode();
        String in = "( 1 + 1 )";
        initParser(in);
        ExpressionNode given = (ExpressionNode) parser.baseExpression();

        assertEquals(expected.getType(), given.getType());
    }

    @Test
    public void parseVariable() throws WrongTokenTypeException, IOException, TokenException {
        Variable expected = new Variable("variable");
        String in = "variable";
        initParser(in);

        Variable given = (Variable) parser.baseExpression();

        assertEquals(expected.getType(), given.getType());
        assertEquals(expected.getId(), given.getId());
    }

    @Test
    public void parsePriorityOperations() throws WrongTokenTypeException, IOException, TokenException {
        String in = "1 * 2 / 3";
        initParser(in);
        ExpressionNode given = (ExpressionNode) parser.priorityOperation();

        assertEquals(TokenType.MULTIPLY, given.getOperations().get(0));
        assertEquals(TokenType.DIVIDE, given.getOperations().get(1));
    }

    @Test
    public void parsePlusAndMinusOperations() throws WrongTokenTypeException, IOException, TokenException {
        String in = "1 + 2 - 3";
        initParser(in);
        ExpressionNode given = (ExpressionNode) parser.expression();

        assertEquals(TokenType.PLUS, given.getOperations().get(0));
        assertEquals(TokenType.MINUS, given.getOperations().get(1));
    }

    @Test
    public void parsePrimaryNotCondition() throws WrongTokenTypeException, IOException, TokenException {
        Condition expected = new Condition();
        expected.setIsNegated(true);
        String in = "!condition";
        initParser(in);

        Condition given = parser.baseCondition();

        assertEquals(expected.isNegated(), given.isNegated());
    }

    @Test
    public void parseRelationalCondition() throws IOException, WrongTokenTypeException, TokenException {
        Condition expected = new Condition();
        expected.setOperator(TokenType.GREATER);
        String in = "variable > 2";
        initParser(in);

        Condition given = parser.relationalCondition();

        assertEquals(expected.getOperator(), given.getOperator());
    }

    @Test
    public void parseEqualCondition() throws IOException, WrongTokenTypeException, TokenException {
        Condition expected = new Condition();
        expected.setOperator(TokenType.EQUAL);
        String in = "variable == 2";
        initParser(in);

        Condition given = parser.equalityCondition();

        assertEquals(expected.getOperator(), given.getOperator());
    }

    @Test
    public void parseOrCondition() throws WrongTokenTypeException, IOException, TokenException {
        Condition condition = new Condition();
        condition.setOperator(TokenType.OR);
        String input = "x || y";
        initParser(input);

        Condition given = parser.condition();

        assertEquals(condition.getOperator(), given.getOperator());
    }

    @Test
    public void parseDeclarationStatement() throws IOException, TokenException, WrongTokenTypeException {
        DeclarationStatement expected = new DeclarationStatement("EUR", "eur");
        String in = "EUR eur;";
        initParser(in);

        parser.progress();
        DeclarationStatement given = (DeclarationStatement) parser.statement();

        assertEquals(expected.getType(), given.getType());
        assertEquals(expected.getReturnType(), given.getReturnType());
        assertEquals(expected.getId(), given.getId());
    }

    @Test
    public void parseFunctionCall() throws WrongTokenTypeException, IOException, TokenException {
        FunctionCall expected = new FunctionCall("functionCall");
        String in = "functionCall(1.0)";
        initParser(in);
        FunctionCall given = (FunctionCall) parser.baseExpression();

        assertEquals(expected.getType(), given.getType());
        assertEquals(expected.getId(), given.getId());
    }

    @Test
    public void parseIfElseStatement() throws IOException, TokenException, WrongTokenTypeException {
        IfStatement expected = new IfStatement(new Condition());
        String in = "if ( condition ) {} else {}";
        initParser(in);

        parser.progress();
        IfStatement given = (IfStatement) parser.statement();

        assertEquals(expected.getType(), given.getType());
        assertEquals(0, given.getIfTrueBlock().getStatements().size());
        assertEquals(0, given.getIfFalseBlock().getStatements().size());
    }

    @Test
    public void parseWhileStatement() throws IOException, TokenException, WrongTokenTypeException {
        WhileStatement expected = new WhileStatement();
        AssignStatement statement = new AssignStatement("variable", new ExpressionNode());
        CodeBlock block = new CodeBlock();
        block.addStatement(statement);
        expected.setWhileBlock(block);
        String in = "while( condition ) {variable = variable + 1;}";
        initParser(in);

        parser.progress();
        WhileStatement given = (WhileStatement) parser.statement();

        assertEquals(expected.getType(), given.getType());
        assertEquals(expected.getWhileBlock().getStatements().get(0).getType(), given.getWhileBlock().getStatements().get(0).getType());
        assertEquals(((AssignStatement) expected.getWhileBlock().getStatements().get(0)).getId(), ((AssignStatement) given.getWhileBlock().getStatements().get(0)).getId());
    }

    @Test
    public void parseReturnStatement() throws IOException, TokenException, WrongTokenTypeException {
        ExpressionNode expressionNode = new ExpressionNode();
        expressionNode.addOperation(TokenType.MINUS);
        ReturnStatement expected = new ReturnStatement(expressionNode);
        String in = "return 1 - 1;";
        initParser(in);

        parser.progress();
        ReturnStatement given = (ReturnStatement) parser.statement();

        assertEquals(expected.getType(), given.getType());
        assertEquals(expected.getExpression().getType(), given.getExpression().getType());
        assertEquals(((ExpressionNode) expected.getExpression()).getOperations().get(0),
                ((ExpressionNode) given.getExpression()).getOperations().get(0));
    }
}
