package truescript.classes;

import java.util.ArrayList;

public class Generator
{
    private final ArrayList<Character> var;
    private final ArrayList<Character> jun;
    private int brackets = 0;
    private int bracketGap = 0;
    private int varCount = 0;
    private boolean notCount = false;

    public Generator()
    {
        var = new ArrayList<>();
        var.add('p');
        var.add('q');
        var.add('r');
        var.add('s');
        var.add('t');
        var.add('u');

        jun = new ArrayList<>();
        jun.add('∨');
        jun.add('∧');
        jun.add('↔');
        jun.add('→');
    }

    public String generate(String length, int count, boolean OR, boolean AND, boolean XNOR, boolean IMP)
    {
        StringBuilder str;
        str = new StringBuilder(rollNot() + rollBracket(30) + rollNot() + rollVariable(count) + closeBracket(30) + jun.get(rollJunktor(OR, AND, XNOR, IMP)));

        for (int i = 3; i <= rollLength(length); i++)
        {
            str.append(rollNot()).append(rollBracket(30)).append(rollNot()).append(rollVariable(count)).append(closeBracket(30)).append(jun.get(rollJunktor(OR, AND, XNOR, IMP)));
        }

        str.append(rollNot()).append(rollVariable(count));

        while (brackets > 0)
        {
            str.append(")");
            brackets--;
        }

        varCount = 0;
        return str.toString();
    }

    public int rollLength(String length)
    {
        return switch (length) {
            case "kurz" -> roll(2, 5);
            case "mittel" -> roll(5, 8);
            case "lang" -> roll(8, 13);
            default -> 2;
        };
    }

    public Character rollVariable(int count)
    {
        bracketGap++;
        notCount = false;

        if(varCount < count)
        {
            varCount++;
            return var.get(varCount - 1);
        }
        else
        {
            return var.get(roll(0, count));
        }
    }

    public int rollJunktor(boolean OR, boolean AND, boolean XNOR, boolean IMP)
    {
        int roll = roll(0, 4);

        if(OR || AND || XNOR || IMP)
        {
            while ((roll == 0 && !OR) || (roll == 1 && !AND) || (roll == 2 && !XNOR) || (roll == 3 && !IMP))
            {
                roll = roll(0, 4);
            }
        }
        else
        {
            roll = roll(0, 4);
        }
        return roll;
    }

    public String rollNot()
    {
        double roll = Math.random() * 100;
        String not;

        if (roll < 20 && !notCount)
        {
            not = "¬";
            notCount = true;
        }
        else
        {
            not = "";
        }
        return not;
    }

    public String rollBracket(int chance)
    {
        double roll = Math.random() * 100;
        String bracket;

        if (roll < chance)
        {
            bracket = "(";
            brackets++;
            notCount = false;
            bracketGap = 0;
        }
        else
        {
            bracket = "";
        }
        return bracket;
    }

    public String closeBracket(int chance)
    {
        double roll = Math.random() * 100;
        String bracket;

        if(brackets > 0 && bracketGap > 1)
        {
            if (roll < chance)
            {
                bracket = ")";
                brackets--;
            }
            else
            {
                bracket = "";
            }
        }
        else
        {
            bracket = "";
        }

        return bracket;
    }

    public int roll(int min, int max)
    {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
