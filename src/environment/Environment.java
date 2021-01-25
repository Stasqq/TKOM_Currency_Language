package environment;

import currency.CurrencyType;
import errors.EnvironmentException;
import gramma.elementary.Function;
import gramma.elementary.Signature;
import gramma.interfaces.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Environment {

    private final Map<String, Function> functions;
    private final ArrayList<Range> ranges;

    private final List<CurrencyType> currenciesTypes;

    public Environment(List<Function> functions, List<CurrencyType> currenciesTypes) throws EnvironmentException {
        this.functions = new HashMap<>();
        for(Function function : functions) {
            if( !this.functions.containsKey(function.getId()) ) {
                this.functions.put(function.getId(), function);
            } else
                throw new EnvironmentException("More than one function with the same id: " + function.getId());
        }
        this.currenciesTypes = currenciesTypes;
        this.ranges = new ArrayList<>();
    }

    public void addFunction(Function function) throws EnvironmentException {
        if (functions.put(function.getId(), function) != null) {
            throw new EnvironmentException("More than one function with the same id: " + function.getId());
        }
    }

    public Function getFunction(String id) throws EnvironmentException {
        if(functions.containsKey(id))
            return functions.get(id);
        else
            throw new EnvironmentException("Unknown reference for: " + id);
    }

    public void setVariable(String id, Value complete) throws EnvironmentException {
        Range range = ranges.get(0);

        if (range.contains(id)) {
            range.setVariable(id, complete);
            return;
        }

        while (range.getParent() != null) {
            range = range.getParent();
            if (range.contains(id)) {
                range.setVariable(id, complete);
                return;
            }
        }

        throw new EnvironmentException("Undefined Reference to:" + id);
    }

    public void addVariable(String id, Value value) throws EnvironmentException {
        Range currentRange = ranges.get(0);
        currentRange.addVariable(id,value);
    }

    public Value getVariable(String id) throws EnvironmentException {
        Range range = ranges.get(0);
        if(range.contains(id)) {
            return range.getVariable(id);
        }

        while( (range = range.getParent()) != null ) {
            if(range.contains(id))
                return range.getVariable(id);
        }

        throw new EnvironmentException("Unknown reference for:" + id);
    }

    public boolean containsCurrencyType(String currencyShortcut) throws EnvironmentException {
        for(CurrencyType currencyType : currenciesTypes)
            if(currencyType.getCurrencyShortcut().equals(currencyShortcut))
                return true;
        throw new EnvironmentException("Unknow datatype " + currencyShortcut);
    }

    public CurrencyType getCurrencyType(String currencyShortcut) throws EnvironmentException {
        if(containsCurrencyType(currencyShortcut))
            for(CurrencyType currencyType : currenciesTypes)
                if(currencyType.getCurrencyShortcut().equals(currencyShortcut))
                    return currencyType;
        throw new EnvironmentException("Unknow datatype" + currencyShortcut);
    }

    public void createNewRange() { ranges.add(0, new Range()); }

    public void createNewLocalRange() {
        Range localRange = new Range();

        if(!ranges.isEmpty())
            localRange.setParent(ranges.get(0));

        ranges.add(0, localRange);
    }

    public void deleteRange() { ranges.remove(0); }
}
