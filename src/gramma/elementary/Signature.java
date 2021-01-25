package gramma.elementary;

import gramma.NodeType;

public class Signature {

    private final String returnType;

    private final String id;

    public Signature(String returnType, String id) {
        this.returnType = returnType;
        this.id = id;
    }

    protected boolean isReturnType(NodeType type) {
        return getReturnType().toUpperCase().equals(type.toString());
    }

    public String getId() { return id; }

    public String getReturnType() { return returnType; }
}
