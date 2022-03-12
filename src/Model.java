package formula;

import java.util.ArrayList;
import java.util.HashMap;

import formula.Formula;
import formula.State;

public class Model {
    public ArrayList<State> states = new ArrayList<State>();
    public ArrayList<State> designatedStates = states;
    public Model parent_model = null; //Parent model in the search tree
    public Action parent_action = null; //Parent action in the search tree

    public Model(ArrayList<State> states) {
        this.states = states;
    }

    public boolean models(Formula f) {

        for(State s: designatedStates){
            if(!f.eval(s)){
                return false;
            }
        }

        return true;
    }

    public String toString() {
        String res = "";
        for (State s : states) {
            res = res + s.toString();
        }
        res = res + "designated: ";
        for(State a: designatedStates){
            res = res + a.name +" ";
        }
        res = res + "\n";

        return res;

    }

    public State get_state_by_name(String name) {
        for (State s : states) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        return null;
    }

    public Model cross_product(Action a) {
        ArrayList<State> new_states = new ArrayList<State>();
        ArrayList<State> desiganed_states = new ArrayList<State>();
        for (State s : states) {
            for (Event e : a.get_events()) {
                // check precondition
                ArrayList<String> pre = e.pre;
                boolean flag = true;
                for (String atom : pre) {
                    if (atom.startsWith("!")) {
                        if (s.evaluation.contains(atom.substring(1))) {
                            flag = false;
                        }
                    } else {

                        if (!s.evaluation.contains(atom)) {
                            flag = atom.equals("T");

                        }
                    }
                }
                if (flag) {
                    State new_state = new State(s.name + e.name);
                    new_state.old_state = s;
                    new_state.old_event = e;
                    if(a.designaedEvents.contains(e) & this.designatedStates.contains(s)){
                        desiganed_states.add(new_state);
                    }
                    for (String atom : s.evaluation) {
                        if (!e.post.contains("!" + atom)) {
                            new_state.putEvalution(atom);
                        }
                    }
                    for (String atom : e.post) {
                        if (!atom.startsWith("!")) {
                            if (!atom.equals("T")) {
                                new_state.putEvalution(atom);
                            }
                        }
                    }

                    new_states.add(new_state);
                }
            }
        }
        for (State s1 : new_states) {
            for (State s2 : new_states) {
                HashMap<String, ArrayList<Event>> s1_old_event_relation = s1.old_event.relation;
                HashMap<String, ArrayList<State>> s1_old_state_relation = s1.old_state.relation;
                
                for (String agent : s1_old_event_relation.keySet()) {
                    
                    if (s1_old_event_relation.get(agent) != null && s1_old_state_relation.get(agent) != null
                            && s1_old_event_relation.get(agent).contains(s2.old_event)
                            && s1_old_state_relation.get(agent).contains(s2.old_state)) {
                        s1.addRelationByAgent(agent, s2);
                    }
                }
            }
        }

        Model m = new Model(new_states);
        m.designatedStates = desiganed_states;

        return m;
    }

    public ArrayList<Model> get_all_cross_product_models(ArrayList<Action> actions){

        ArrayList<Model> res = new ArrayList<Model>();
        for(Action a: actions){
            Model product = this.cross_product(a);
            if(product.states.size() != 0){
                product.parent_action = a;
                product.parent_model = this;
                res.add(product);
            }
        }
        return res;
    }

}
