package inputReaders;

import currency.Currency;
import errors.CurrenciesFileException;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;

//TODO: obsluga bledow pliku, teraz dziala z zalozeniem poprawnych plikow

public class CurrenciesScanner {

    private Scanner scanner;
    private int currenciesNumber;
    private List<String> currenciesNames;

    public CurrenciesScanner(String filePath)
    {
        try {
            this.scanner = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<String> readCurrenciesNameList()
    {
        String line = scanner.nextLine();
        String[] names = line.split("\\s+");
        this.currenciesNames = Arrays.asList(names);
        return this.currenciesNames;
    }

    public ArrayList<currency.Currency> readCurrencies() throws CurrenciesFileException {
        this.currenciesNumber = currenciesNames.size();
        return readCurrenciesWithRatio(currenciesNames);
    }

    private ArrayList<currency.Currency> readCurrenciesWithRatio(List<String> currenciesNames) throws CurrenciesFileException {
        ArrayList<currency.Currency> currenciesMap = new ArrayList<>();
        for(String currencyName: currenciesNames)
        {
            currenciesMap.add(readSingleCurrency(currencyName));
        }
        return currenciesMap;
    }

    private currency.Currency readSingleCurrency(String currencyName) throws CurrenciesFileException {
        HashMap<String, BigDecimal> ratioMap = new HashMap<>();
        if(scanner.next().equals(currencyName))
        {
            for(int ratioCounter=0; ratioCounter < currenciesNumber; ratioCounter++)
            {
                ratioMap.put(currenciesNames.get(ratioCounter),new BigDecimal(scanner.next()));
            }
        }
        else
        {
            throw new CurrenciesFileException();
        }
        return new currency.Currency(currencyName, ratioMap);
    }
}
