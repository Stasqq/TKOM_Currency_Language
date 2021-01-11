import currency.CurrencyType;
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
            ArrayList<CurrencyType> currencies = currenciesScanner.readCurrencies();

            Assert.assertEquals(currenciesNames.size(),6);

            for(CurrencyType currencyType : currencies)
            {
                System.out.println(currencyType.getCurrencyShortcut());
                for(String currencyName : currenciesNames)
                {
                    System.out.print(currencyType.getRatio(currencyName)+" ");
                }
                System.out.println();
            }
        }catch(CurrenciesFileException e)
        {
            e.printStackTrace();
        }
    }
}
