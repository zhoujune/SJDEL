package formula;

public abstract class Formula {
    public abstract String toString();
    public abstract boolean eval(State s);
}

