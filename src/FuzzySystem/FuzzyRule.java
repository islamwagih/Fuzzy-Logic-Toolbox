package FuzzySystem;


import java.util.ArrayList;

public class FuzzyRule
{
    public ArrayList<Pair> input, output;
    public ArrayList<String> inSymbol, outSymbol;

    FuzzyRule()
    {
        input = new ArrayList<>();
        output = new ArrayList<>();
        inSymbol = new ArrayList<>();
        outSymbol = new ArrayList<>();
    }

    public void addInputRule(String name, String value)
    {
        input.add(new Pair(name, value));
    }

    public void addOutputRule(String name, String value)
    {
        output.add(new Pair(name, value));
    }

    public void addInSymbol(String symbol)
    {
        inSymbol.add(symbol);
    }

    public void addOutSymbol(String symbol)
    {
        outSymbol.add(symbol);
    }
}
