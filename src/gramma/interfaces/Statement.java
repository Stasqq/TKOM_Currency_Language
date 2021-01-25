package gramma.interfaces;

import environment.Environment;
import errors.EnvironmentException;
import gramma.elementary.StatementOutput;

public interface Statement  extends Node {
    StatementOutput execute(Environment environment) throws EnvironmentException;
}
