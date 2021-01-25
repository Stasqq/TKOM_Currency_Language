import environment.Environment;
import errors.CurrenciesFileException;
import errors.EnvironmentException;
import errors.TokenException;
import errors.WrongTokenTypeException;
import gramma.elementary.Program;
import inputReaders.CurrenciesScanner;
import lexer.Lexer;
import parser.Parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args)
    {
        if(args.length != 2) {
            System.out.println("Wrong arguments number, you need to give:\nprogram file and currencies file");
            System.exit(0);
        }

        try{
            CurrenciesScanner currenciesScanner = new CurrenciesScanner(args[1]);
            Lexer lexer = new Lexer(new FileReader(args[0]), currenciesScanner.readCurrenciesNameList());
            Parser parser = new Parser(lexer);
            Program program = parser.program();
            Environment environment = new Environment(program.getFunctions(), currenciesScanner.readCurrencies());
            environment.getFunction("main").execute(environment, new ArrayList<>());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WrongTokenTypeException e) {
            e.printStackTrace();
        } catch (CurrenciesFileException e) {
            e.printStackTrace();
        } catch (EnvironmentException e) {
            e.printStackTrace();
        } catch (TokenException e) {
            e.printStackTrace();
        }
    }
}
