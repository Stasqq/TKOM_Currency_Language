package gramma.interfaces;

import environment.Environment;
import errors.EnvironmentException;

public interface Expression extends Node {
    Value evaluate(Environment environment) throws EnvironmentException;
}
