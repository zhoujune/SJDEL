package formula;

public class NegationFormula extends Formula{

    private Formula name;

    public NegationFormula(Formula name) {
        this.name = name;
    }

    public Formula getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "!"+"("+name+")";
    }

    @Override
    public boolean eval(State s){
        return !name.eval(s);
    }
}

