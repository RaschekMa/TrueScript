package truescript.classes;

public class Calculator
{
    char[] statement;

    public Calculator(String input)
    {
        statement = input.toCharArray();
    }

    //------------------------------------------------CalcMethods-------------------------------------------------------

    //Diese Methode kalkuliert den kompletten übergebenen char[]. Positionen der möglich vorhandenen Klammern werden
    //mit getPosBracketStart() und getPosBracketEnd() bestimmt. Diese werden benötigt, damit das Ergebnis einer Klammer
    //richtig mit swapBracketResult() wieder eingefügt werden kann. Kalkulation siehe calcTerm().

    public char calc(char[] a)
    {
        char[] x = copyCharArray(a);

        while(x.length > 1)
        {
            int start = getPosBracketStart(x);
            int end = getPosBracketEnd(x);
            char result = calcTerm(calcNOT(solveBrackets(x)));

            if(bracket(x))
            {
                x = swapBracketResult(x, start, end, result);
            }
            else
            {
                return result;
            }
        }

        return x[0];
    }

    //Mit getJunPos() wird die Position des zu lösenden Junktors ermittelt. Im Anschluss wird dieser samt den beiden betroffenen
    //Positionen (zB 1v1) aus dem übergebenen char[] ausgelesen und berechnet. swapResult() tauscht Ergebnis mit vorheriger
    //Position aus.

    public char calcTerm(char[] a)
    {
        char[] x = copyCharArray(a);

        while(x.length > 1)
        {
            int pos = getJunPos(x);
            boolean b = false;

            if(x[pos] == '∧')
            {
                b = calcAND(intToBool(x[pos - 1]), intToBool(x[pos + 1]));
            }

            if(x[pos] == '∨')
            {
                b = calcOR(intToBool(x[pos - 1]), intToBool(x[pos + 1]));
            }

            if(x[pos] == '↔')
            {
                b = calcXNOR(intToBool(x[pos - 1]), intToBool(x[pos + 1]));
            }

            if(x[pos] == '→')
            {
                b = calcIMP(intToBool(x[pos - 1]), intToBool(x[pos + 1]));
            }

            x = swapResult(x, pos, returnCharResult(b));
        }

        return x[0];
    }

    //Tauscht result im übergebenen char[] an der übergebenen Postion aus.

    public char[] swapResult(char[] input, int pos, char result)
    {
        char[] x = new char[input.length - 2];

        if (pos >= 0) System.arraycopy(input, 0, x, 0, pos);

        x[pos - 1] = result;

        if(x.length > 1)
        {
            for(int i = pos; i < x.length; i++)
            {
                x[i] = input[i + 2];
            }
        }

        return x;
    }

    //Das gleiche wie swapResult, zusätzlich werden Klammern berücksichtigt.

    public char[] swapBracketResult(char[] input, int pos, int end, char result)
    {
        int length = end - pos;
        char[] x = new char[input.length - length];


        for(int i = 0; i < pos; i++)
        {
            x[i] = input[i];
        }

        x[pos] = result;

        if(x.length > 1)
        {
            for(int i = pos + 1; i < x.length; i++)
            {
                x[i] = input[i + length];
            }
        }

        return x;
    }

    //Zählt im übergegebenen char[] die Anzahl der '¬'. Da diese entfernt und die Stellen nach einem '¬' invertiert werden,
    //wird das zurückgegebene char[] um die Anzahl der '¬' verkleinert.

    public char[] calcNOT(char[] a)
    {
        int countNOT = 0;
        int count = 0;

        for(int i = 0; i < a.length; i++)
        {
            if(a[i] == '¬')
            {
                countNOT++;
            }
        }

        char[] x = new char[a.length - countNOT];

        for(int i = 0; i < x.length; i++)
        {
            if(a[i+count] == '¬')
            {
                count++;
                x[i] = invertChar(a[i+count]);
            }
            else
            {
                x[i] = a[i+count];
            }
        }
        return x;
    }

    //Liest die Positionen der zu berechnenden Klammern eines char[] aus. Der char[] innerhalb der Klammern wird ohne Klammern
    //zurückgegeben.

    public char[] solveBrackets(char[] a)
    {
        int begin = 0;
        int end = 0;

        for(int i = 0; i < a.length; i++)
        {
            if(a[i] == '(')
            {
                begin = i;
            }
            if(a[i] == ')')
            {
                end = i;
                break;
            }
        }

        if(end > begin)
        {
            char[] x = new char[end - (begin + 1)];

            for(int i = 0; i < x.length; i++)
            {
                x[i] = a[begin + i + 1];
            }
            return x;
        }
        return a;
    }

    //Ermittelt je nach Priorität die Position des zu berechnenden Junktors.

    public int getJunPos(char[] a)
    {
        int junPos = 0;

        for(int i = 0; i < a.length; i++)
        {
            if(a[i] == '∧')
            {
                junPos = i;
                return junPos;
            }
        }

        for(int i = 0; i < a.length; i++)
        {
            if(a[i] == '∨')
            {
                junPos = i;
                return junPos;
            }
        }

        for(int i = 0; i < a.length; i++)
        {
            if(a[i] == '→')
            {
                junPos = i;
                return junPos;
            }
        }

        for(int i = 0; i < a.length; i++)
        {
            if(a[i] == '↔')
            {
                junPos = i;
                return junPos;
            }
        }

        return 0;
    }

    //Ermittelt die Position der zu berücksichtigenden Startklammer.

    public int getPosBracketStart(char[] a)
    {
        int begin = 0;

        for(int i = 0; i < a.length; i++)
        {
            if(a[i] == '(')
            {
                begin = i;
            }
            if(a[i] == ')')
            {
                break;
            }
        }
        return begin;
    }

    public boolean bracket(char[] a)
    {
        boolean bracket = false;

        for(int i = 0; i < a.length; i++)
        {
            if(a[i] == '(')
            {
                bracket = true;
            }
        }
        return bracket;
    }

    public int getPosBracketEnd(char[] a)
    {
        int end = 0;

        for(int i = 0; i < a.length; i++)
        {
            if(a[i] == ')')
            {
                end = i;
                break;
            }
        }
        return end;
    }

    public char[] copyCharArray(char[] a)
    {
        char[] x = new char[a.length];

        for(int i = 0; i < a.length; i++)
        {
            x[i] = a[i];
        }
        return x;
    }

    //------------------------------------------------Operators---------------------------------------------------------

    public boolean intToBool(int a)
    {
        boolean x = false;

        if(a == '1')
        {
            x = true;
        }
        else
        {
            x = false;
        }
        return x;
    }

    public char returnCharResult(boolean a)
    {
        char cha = ' ';

        if(a == true)
        {
            cha = '1';
        }
        else
        {
            cha = '0';
        }
        return cha;
    }

    public char invertChar(char a)
    {
        if(a == '1')
        {
            return '0';
        }
        else
        {
            return '1';
        }
    }

    public boolean calcOR(boolean a, boolean b)
    {
        boolean result = false;

        if(a || b)
        {
            result = true;
        }
        return result;
    }

    public boolean calcAND(boolean a, boolean b)
    {
        boolean result = false;

        if(a && b)
        {
            result = true;
        }
        return result;
    }

    public boolean calcXNOR(boolean a, boolean b)
    {
        boolean result = false;

        if(a == b)
        {
            result = true;
        }
        return result;
    }

    public boolean calcXOR(boolean a, boolean b)
    {
        boolean result = false;

        if(a^b)
        {
            result = true;
        }
        return result;
    }

    public boolean calcIMP(boolean a, boolean b)
    {
        boolean result = false;

        if(a && !b)
        {
            result = false;
        }
        else
        {
            result = true;
        }
        return result;
    }
}
