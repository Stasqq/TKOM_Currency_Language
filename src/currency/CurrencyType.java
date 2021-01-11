package currency;

import java.math.BigDecimal;
import java.util.HashMap;

public class CurrencyType {
    private String currencyShortcut;
    private HashMap<String, BigDecimal> exchange;

    public CurrencyType(String currencyShortcut, HashMap<String, BigDecimal> exchange)
    {
        this.currencyShortcut = currencyShortcut;
        this.exchange = exchange;
    }

    public String getCurrencyShortcut() {
        return currencyShortcut;
    }

    public double getRatio(String otherCurrencyName)
    {
        return exchange.get(otherCurrencyName).doubleValue();
    }
}
