package formula;

public class BoxFormula extends Formula{

    private Formula name;
    private String agent;

    public BoxFormula(Formula name, String agent) {
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
        return "K_"+agent+"("+name+")";
    }

    @Override
    public boolean eval(State s){
        for(State st:s.getAccessiblStatesByAgent(agent)){
            if(!name.eval(st)){
                return false;
            }
        }
        return true;
    }

}

