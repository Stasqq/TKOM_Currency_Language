package gramma.interfaces;

import environment.Environment;

public interface Expression extends Node {
    Value evaluate(Environment environment);
}
