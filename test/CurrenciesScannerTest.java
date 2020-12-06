import currency.Currency;
import errors.CurrenciesFileException;
import inputReaders.CurrenciesScanner;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CurrenciesScannerTest {

    @Test
    public void test()
    {
        try{
            CurrenciesScanner currenciesScanner = new CurrenciesScanner("inputs/currencies.txt");
            List<String> currenciesNames = currenciesScanner.readCurrenciesNameList();
            ArrayList<Currency> currencies = currenciesScanner.readCurrencies();

            Assert.assertEquals(currenciesNames.size(),6);

            for(currency.Currency currency : currencies)
            {
                System.out.println(currency.getCurrencyShortcut());
                for(String currencyName : currenciesNames)
                {
                    System.out.print(currency.getRatio(currencyName)+" ");
                }
                System.out.println();
            }
        }catch(CurrenciesFileException e)
        {
            e.printStackTrace();
        }
    }
}
