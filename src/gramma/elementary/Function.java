package gramma.elementary;

import environment.Environment;
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

    public Value execute(Environment environment, List<Expression> arguments) {
        return null;
    }

    public void addParameter(Signature parameter) { parameters.add(parameter); }

    public List<Signature> getParameters() { return parameters; }

    @Override
    public NodeType getType() {
        return NodeType.FUNCTION;
    }

    public void setCodeBlock(CodeBlock codeBlock) { this.codeBlock = codeBlock; }

}
