package truescript.classes;

import java.util.ArrayList;
import java.util.Collections;

public class Statement
{
    private String fullStatement;
    private ArrayList<Character> var;
    private char[] variables;
    private int varCount;
    private boolean[][] tMatrix;
    private char[] terms;

    public Statement(String input)
    {
        fullStatement = input;
        var = getVars(cleanInput(input));
        varCount = var.size();
        tMatrix = createMatrix(varCount);
        variables = getCharVariables();
        terms = solveTerms(setStatements());
    }

    //Erstellt Objekt Calculator, convertiert den übergebenen String[] in einen char[] und berechnet diesen mit calc().

    public char[] solveTerms(String[] a)
    {
        Calculator calc = new Calculator(fullStatement);
        char[] x = new char[(int)Math.pow(2.0, varCount)];

        for(int i = 0; i < a.length; i++)
        {
            char[] b = a[i].toCharArray();
            x[i] = calc.calc(b);
        }
        return x;
    }

    public char[] getCharVariables()
    {
        char[] a = new char[varCount];

        for(int i = 0; i < varCount; i++)
        {
            a[i] = var.get(i);
        }
        return a;
    }

    public String[] setStatements()
    {
        String[] a = new String[(int)Math.pow(2.0, varCount)];

        for(int i = 0; i < a.length; i++)
        {
            a[i] = getStatements(i);
        }
        return a;
    }

    //Ersetzt für jede Möglichkeit der Aussage die Variablen mit 1 oder 0 je nachdem wie der Wert in tMatrix für diese Variable vorgegeben ist.
    //zB: p q    Formel: p v q
    //    1 1 -> p und q werden mit char '1' ersetzt: 1 v 1
    //    1 0
    //    0 1
    //    0 0

    public String getStatements(int x)
    {
        char[] a = fullStatement.toCharArray();
        StringBuilder str = new StringBuilder();

        for(int i = 0; i < a.length; i++)
        {
            for(int j = 0; j < variables.length; j++)
            {
                if(a[i] == variables[j])
                {
                    if(tMatrix[x][j])
                    {
                        a[i] = '1';
                    }
                    else
                    {
                        a[i] = '0';
                    }
                }
            }
        }

        for (char c : a)
        {
            str.append(c);
        }
        return str.toString();
    }

    //Der eingelesene String[] wird in char[] convertiert und jeder char einmalig in einer Arraylist<Character> aufgenommen.
    //Im Anschluss werden die chars in der List sortiert.

    public ArrayList<Character> getVars(String statement)
    {
        char[] x = statement.toCharArray();
        ArrayList<Character> y = new ArrayList<>();

        for (char c : x)
        {
            if (!y.contains(c))
            {
                y.add(c);
            }
        }
        Collections.sort(y);

        return y;
    }

    public String cleanInput(String statement)
    {
        return statement.replaceAll("[^a-zA-Z]", "");
    }

    public boolean[][] createMatrix(int number)
    {
        boolean[][] x = new boolean[(int)Math.pow(2.0, number)][number];
        int y = (int)Math.pow(2.0, number);
        int count = 1;

        for(int i = number - 1; i >= 0; i--)
        {
            int counter0 = 0;
            int counter1 = 0;

            for(int j = 0; j < y; j++)
            {
                if(counter1 == 0)
                {
                    if(counter0 < count)
                    {
                        x[j][i] = true;
                        counter0++;
                    }
                    else
                    {
                        counter1 = count - 1;
                        counter0 = 0;
                    }
                }
                else
                {
                    counter1--;
                }
            }
            count = count * 2;
        }
        return x;
    }

    public String toString()
    {
        String str = "-----------------Calculate-----------------\n";
        str += "Statement: " + fullStatement + "\n";
        str += "Variables " + var.toString() + "\n";
        str += "Count: " + varCount + "\n" + "\n";

        return str;
    }

    public String toStringMatrix()
    {
        StringBuilder str = new StringBuilder();

        for(int i = 0; i < varCount; i++)
        {
            str.append(var.get(i).toString()).append(" ");
        }

        str.append("\n");

        for(int i = 0; i < Math.pow(2.0, varCount); i++)
        {
            for(int j = 0; j < varCount; j++)
            {
                if(tMatrix[i][j])
                {
                    str.append("1" + " ");
                }
                else
                {
                    str.append("0" + " ");
                }
            }
            str.append("\n");
        }

        return str.toString();
    }

    public String toStringResult()
    {
        StringBuilder str = new StringBuilder();

        str.append("Result\n");

        for (char term : terms)
        {
            str.append(term).append("\n");
        }

        return str.toString();
    }
}
