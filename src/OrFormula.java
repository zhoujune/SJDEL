package formula;

public class OrFormula extends Formula{

    private Formula left;
    private Formula right;

    public OrFormula(Formula leftFormula, Formula rightFormula) {
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
        return "(" + left + "|" + right + ")";
    }

    @Override
    public boolean eval(State s){
        return left.eval(s) | right.eval(s);
    }

}

