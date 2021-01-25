package gramma.elementary;

import environment.Environment;
import errors.EnvironmentException;
import gramma.NodeType;
import gramma.interfaces.Expression;
import gramma.interfaces.Node;
import gramma.interfaces.Value;

import java.util.ArrayList;
import java.util.List;

public class Function extends Signature implements Node {

    private List<Signature> parameters;
    private CodeBlock codeBlock;

    public Function(String returnType, String id) {
        super(returnType, id);
        this.parameters = new ArrayList<>();
    }

    public Value execute(Environment environment, List<Expression> arguments) throws EnvironmentException {
        Value returnValue = executeFunction(environment, arguments);
        return castReturnedType(returnValue);
    }

    private Value executeFunction(Environment environment, List<Expression> arguments) throws EnvironmentException {
        prepareEnvironment(environment, arguments);

        StatementOutput output = codeBlock.execute(environment);
        environment.deleteRange();

        if (!output.isStatusReturn()) {
            throw new EnvironmentException("This function need return statement");
        }

        return output.getReturnValue();
    }

    private void prepareEnvironment(Environment environment, List<Expression> arguments) throws EnvironmentException {
        checkParametersNumber(arguments);

        List<Value> argumentsValue = prepareArguments(environment, arguments);

        environment.createNewRange();

        addArgumentsToEnvironment(environment, argumentsValue);
    }

    private void checkParametersNumber(List<Expression> arguments) throws EnvironmentException {
        if (arguments.size() != parameters.size()) {
            throw new EnvironmentException("Wrong arguments number");
        }
    }

    private List<Value> prepareArguments(Environment environment, List<Expression> arguments) throws EnvironmentException {
        List<Value> argumentsValue = new ArrayList<>();
        for (Expression arg : arguments) {
            argumentsValue.add(arg.evaluate(environment));
        }

        return argumentsValue;
    }

    private void addArgumentsToEnvironment(Environment environment, List<Value> argumentsValue) throws EnvironmentException {
        for (int i = 0; i < parameters.size(); i++) {
            Signature parameter = parameters.get(i);
            Value argument = checkArgument(parameter, argumentsValue.get(i));

            environment.addVariable(parameter.getId(), argument);
        }
    }

    private Value checkArgument(Signature parameter, Value argument) throws EnvironmentException {
        if(parameter.isReturnType(NodeType.DOUBLE) && Value.isInt(argument))
            return new DoubleNode(((IntNode) argument).getValue());

        if (!(parameter.isReturnType(argument.getType()) || Value.isCurrency(argument))) {
            throw new EnvironmentException("Unexpected argument type. Expected: " + parameter.getReturnType().toUpperCase() + " actual: " + argument.getType());
        }

        return argument;
    }

    private Value castReturnedType(Value returnValue) throws EnvironmentException {
        if (Value.isCurrency(returnValue)) {
            return new Currency(((Currency)returnValue).getCurrencyType(), ((Currency)returnValue).getValue());
        } else if (Value.isInt(returnValue) && isReturnType(NodeType.DOUBLE)) {
            return new DoubleNode(((IntNode) returnValue).getValue());
        } else if (isReturnType(returnValue.getType())) {
            return returnValue;
        }

        throw new EnvironmentException("Unexpected return type. Expected: " + getReturnType().toUpperCase() + " actual: " + returnValue.getType());
    }

    public void addParameter(Signature parameter) { parameters.add(parameter); }

    public List<Signature> getParameters() { return parameters; }

    @Override
    public NodeType getType() {
        return NodeType.FUNCTION;
    }

    public void setCodeBlock(CodeBlock codeBlock) { this.codeBlock = codeBlock; }

}
