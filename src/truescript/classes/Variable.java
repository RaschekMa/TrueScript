package truescript.classes;

public class Variable implements Comparable<Variable>
{
    private char var;

    public Variable()
    {

    }

    public Variable(char a)
    {
        var = a;
    }

    public char getChar()
    {
        return var;
    }

    @Override
    public String toString()
    {
        String str = "";
        str += var;
        return str;
    }

    @Override
    public int compareTo(Variable o)
    {
        if(o.var == this.var)
        {
            return 0;
        }
        else
        {
            return -1;
        }
    }
}
