package formula;

public class ConjunctionFormula extends Formula{

    private Formula left;
    private Formula right;

    public ConjunctionFormula(Formula leftFormula, Formula rightFormula) {
        this.left = leftFormula;
        this.right = rightFormula;
    }

    public Formula getLeft() {
        return left;
    }

    public Formula getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "(" + left + "&" + right + ")";
    }

    @Override
    public boolean eval(State s){
        return left.eval(s) & right.eval(s);
    }

}

