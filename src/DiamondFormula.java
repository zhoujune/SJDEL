package formula;

public class DiamondFormula extends Formula{

    private Formula name;
    private String agent;

    public DiamondFormula(Formula name, String agent) {
        this.name = name;
        this.agent = agent;
    }

    public Formula getName() {
        return this.name;
    }

    public String getAgent() {
        return this.agent;
    }

    @Override
    public String toString() {
        return "M_"+agent+"("+name+")";
    }

    @Override
    public boolean eval(State s){
        for(State st:s.getAccessiblStatesByAgent(agent)){
            if(name.eval(st)){
                return true;
            }
        }
        return false;
    }
}

