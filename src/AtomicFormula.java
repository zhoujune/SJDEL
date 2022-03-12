package formula;

public class AtomicFormula extends Formula{

    private String name;

    public AtomicFormula(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean eval(State s){
        return s.containsAtomicFormula(this.name);
    }
}

