package formula;

import java.util.ArrayList;
import java.util.HashMap;

public class State {
    public String name;
    public HashMap<String, ArrayList<State>> relation = new HashMap<String, ArrayList<State>>();
    public ArrayList<String> evaluation  = new ArrayList<String>();

    public State old_state = null;
    public Event old_event = null;

    public State(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setRelation(HashMap<String, ArrayList<State>> relation){
        this.relation = relation;
    }

    public void addRelationByAgent(String agent, State s){
        if(!relation.containsKey(agent)){
            relation.put(agent, new ArrayList<State>());
        }
        relation.get(agent).add(s);
    }

    public void setEvaluation(ArrayList<String> evaluation){
        this.evaluation = evaluation;
    }

    public void putEvalution(String f){
        evaluation.add(f);
    }

    public ArrayList<State> getAccessiblStatesByAgent(String agent){
        return relation.get(agent);
    }

    public boolean containsAtomicFormula(String f){
        for(String s: evaluation){
            if(s.equals(f)){
                return true;
            }
        }
        return false;
        
    }

    public boolean models(Formula formula){
        return evaluation.contains(formula);
    } 

    public String toString(){
        String res = name+":"+evaluation.toString()+"\n";
        for(String key:relation.keySet()){
            res = res+"  "+key+":";
            for(State s: relation.get(key)){

                
                res = res + s.getName() + ",";
            }
            res = res + "\n";

        }
        return res;

    }
    
}
