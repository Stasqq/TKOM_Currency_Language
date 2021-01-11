package gramma.elementary;

public class Signature {

    private final String returnType;

    private final String id;

    public Signature(String returnType, String id) {
        this.returnType = returnType;
        this.id = id;
    }

    public String getId() { return id; }

    public String getReturnType() { return returnType; }
}
