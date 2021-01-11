package gramma.elementary;

import gramma.NodeType;
import gramma.interfaces.Node;

import java.util.ArrayList;
import java.util.List;

public class Program implements Node {

    private List<Function> functions;

    public Program() { this.functions = new ArrayList<>(); }

    public void addFunction(Function function) { functions.add(function); }

    @Override
    public NodeType getType() { return NodeType.PROGRAM; }

    public List<Function> getFunctions() { return functions; }
}
