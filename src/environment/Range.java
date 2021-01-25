package environment;

import errors.EnvironmentException;
import gramma.interfaces.Value;

import java.util.HashMap;
import java.util.Map;

public class Range {
    private Range parent;
    private Map<String, Value> variables;

    public Range() { this.variables = new HashMap<>(); }

    public boolean contains(String id) { return variables.containsKey(id); }

    public void setVariable(String id, Value value) throws EnvironmentException {
        if(variables.replace(id,value) == null)
            throw new EnvironmentException("Undefined Reference to:" + id);
    }

    public void addVariable(String id, Value value) throws EnvironmentException {
        if(variables.put(id,value) != null)
            throw new EnvironmentException("Variable with id "+ id +" already exist");
    }

    public Value getVariable(String id) { return variables.get(id); }

    public void setParent(Range parent) {
        this.parent = parent;
    }

    public Range getParent() { return parent; }
}
