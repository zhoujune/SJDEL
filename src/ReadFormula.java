package formula;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import formula.Action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;

public class ReadFormula {

    public static void main(String args[]) {
        if (args.length < 1) {
            System.err.println("expected one parameter");
            System.exit(1);
        }
        Boolean detail = false;
        if (args.length == 2){
            String filename1 = args[0];
            if(args[1].equals("-v")){
                detail = true;
            }
        }
        String filename1 = args[0];

        //String filename2 = args[1];
        //String filename3 = args[2];
        CharStream inputStream1 = null;
        ArrayList<String> modelInput = new ArrayList<String>();
        ArrayList<String> actionInput = new ArrayList<String>();
        ArrayList<String> searchInput = new ArrayList<String>();
        
        try {

            //inputStream1 = CharStreams.fromFileName(filename1);
            Scanner sc = new Scanner(new File(filename1));
            //Scanner sc2 = new Scanner(new File(filename3));
            while(sc.hasNextLine()){
                searchInput.add(sc.nextLine());
                //modelInput.add(sc.nextLine());
            }
            
            /*while(sc2.hasNextLine()){
                actionInput.add(sc2.nextLine());
            }*/
        }
        catch (IOException e) {
            System.err.println("failed to read file: " + e.getMessage());
            System.exit(1);
        }

        /*FormulaLexer          lexer   = new FormulaLexer(inputStream1);  // lexer breaks up input into tokens
        CommonTokenStream     tokens  = new CommonTokenStream(lexer);   // antlr's datatype for a stream of tokens
        FormulaParser         parser  = new FormulaParser(tokens);      // the parser that implements the grammar
        ParseTree             tree    = parser.formula();               // run the parser to make a parse tree
        FormulaCustomVisitor  visitor = new FormulaCustomVisitor();     // the visitor will traverse the tree
        Formula               formula = (Formula) visitor.visit(tree);  // run the visitor on the tree

        System.out.println(formula);

        Model m = getModelByInput(modelInput);
        
        Action a = getActionByInput(actionInput);

        Model new_m = m.cross_product(a);
        System.out.println("New Model:");
        System.out.println(new_m);

        System.out.println(new_m.models(formula));*/

        Search s = getSearchByInput(searchInput);
        s.detail = detail;
        s.BFS();


    }

    //convert string into Formula
    public static Formula getFormula(String input){

        CharStream stream = CharStreams.fromString(input);
        FormulaLexer          lexer   = new FormulaLexer(stream);  // lexer breaks up input into tokens
        CommonTokenStream     tokens  = new CommonTokenStream(lexer);   // antlr's datatype for a stream of tokens
        FormulaParser         parser  = new FormulaParser(tokens);      // the parser that implements the grammar
        ParseTree             tree    = parser.formula();               // run the parser to make a parse tree
        FormulaCustomVisitor  visitor = new FormulaCustomVisitor();     // the visitor will traverse the tree
        Formula               formula = (Formula) visitor.visit(tree);  // run the visitor on the tree
        return formula;
    }


    //Generate Model 
    public static Model getModelByInput(List<String> input){
        ArrayList<State> states = new ArrayList<State>();
        ArrayList<State> desgnated_states = new ArrayList<State>();
        
        for(String i: input){
            if(i.trim().startsWith("S")){//States
                String info = i.split("=")[1].trim();
                String statesString = info.substring(1, info.length()-1);
                for(String s: statesString.split(",")){
                    states.add(new State(s.trim()));
                }
            }
            if(i.trim().startsWith("D")){//Designated States
                String info = i.split("=")[1].trim();
                String statesString = info.substring(1, info.length()-1);
                for(String s: statesString.split(",")){
                    for(State st: states){
                        if(st.name.equals(s.trim())){
                            desgnated_states.add(st);
                        }
                    }
                }
            }
            if(i.trim().startsWith("R")){//Relations
                String agent = i.substring(2, 3);
                String info = i.split("=")[1].trim().replaceAll("\\(|\\)|\\{|\\}", "");
                String[] sts = info.split(",");
                int index = 0;
                while(index < sts.length){
                    if(sts.length == 1){
                        break;
                    }
                    State fromState = null;
                    State toState = null;
                    for(State s: states){
                        if(s.getName().equals(sts[index].trim())){
                            fromState = s;
                        }
                        if(s.getName().equals(sts[index+1].trim())){
                            toState = s;
                        }
                    }
                    fromState.addRelationByAgent(agent, toState);
                    index += 2;
                }
            }
            if(i.trim().startsWith("L")){//Evaluations
                
                String[] splits = i.split("=");
                String stateName = splits[0].trim().substring(2);
                String info = splits[1].replaceAll("\\{|\\}", "");
                for(String prop: info.split(",")){
                    for(State s: states){
                        if(s.getName().equals(stateName)){
                            s.putEvalution(prop.trim());
                        }
                    }
                }
            }
        }

        Model m = new Model(states);
        m.designatedStates = desgnated_states;
        return m;
    }


    public static Action getActionByInput(List<String> input){
        
        ArrayList<Event> events = new ArrayList<Event>();
        ArrayList<Event> desgnated_events = new ArrayList<Event>();
        String name = "";
        
        for(String i: input){
            if(i.trim().startsWith("action")){//name
                String info = i.split(":")[1].trim();
                name = info;
            }

            if(i.trim().startsWith("E")){//Events
                String info = i.split("=")[1].trim();
                String eventsString = info.substring(1, info.length()-1);
                for(String s: eventsString.split(",")){
                    events.add(new Event(s.trim()));
                }
            }
            if(i.trim().startsWith("D")){//Designated Events
                String info = i.split("=")[1].trim();
                String eventsString = info.substring(1, info.length()-1);
                for(String s: eventsString.split(",")){
                    for(Event st: events){
                        if(st.name.equals(s.trim())){
                            desgnated_events.add(st);
                        }
                    }
                }
            }

            if(i.trim().startsWith("R")){//Relations
                String agent = i.substring(2, 3);
                String info = i.split("=")[1].trim().replaceAll("\\(|\\)|\\{|\\}", "");
                String[] sts = info.split(",");
                int index = 0;
                while(index < sts.length){
                    Event fromEvent = null;
                    Event toEvent = null;
                    for(Event s: events){
                        if(s.getName().equals(sts[index].trim())){
                            fromEvent = s;
                        }
                        if(s.getName().equals(sts[index+1].trim())){
                            toEvent = s;
                        }
                    }
                    fromEvent.addRelationByAgent(agent, toEvent);
                    index += 2;
                }
            }
            if(i.trim().startsWith("pre")){//pre_condition
                
                String info = i.split("=")[1].trim().replaceAll("\\(|\\)|\\{|\\}", "");
                String[] sts = info.split(",");

                int index = 0;
                while(index < sts.length){
                    for(Event s: events){
                        if(s.getName().equals(sts[index].trim())){
                            s.putPre(sts[index+1].trim());
                        }
                    }
                    index += 2;
                }
            }
            if(i.trim().startsWith("post")){//post_condition
                
                String info = i.split("=")[1].trim().replaceAll("\\(|\\)|\\{|\\}", "");
                String[] sts = info.split(",");

                int index = 0;
                while(index < sts.length){
                    for(Event s: events){
                        if(s.getName().equals(sts[index].trim())){
                            s.putPost(sts[index+1].trim());
                        }
                    }
                    index += 2;
                }
            }
        }
        Action a = new Action(events);
        a.name = name;
        a.designaedEvents = desgnated_events;

        return a;
    } 


    public static Search getSearchByInput(List<String> input){
        Formula goal = getFormula(input.get(0));
        ArrayList<Action> actions = new ArrayList<Action>();

        ArrayList<Integer> tick = new ArrayList<Integer>();
        
        for(int i = 1; i< input.size(); i++){
            if(input.get(i).startsWith("initial")){
                tick.add(i);
            }
            if(input.get(i).startsWith("action")){
                tick.add(i);
            }
        }
        tick.add(input.size());
        Model initial = getModelByInput(input.subList(tick.get(0), tick.get(1)));
        for(int i = 1; i< tick.size()-1; i++){
            actions.add(getActionByInput(input.subList(tick.get(i), tick.get(i+1))));
        }
        

        //System.out.println(initial.cross_product(actions.get(2)).cross_product(actions.get(0)));
        //System.out.println(initial.cross_product(actions.get(2)).cross_product(actions.get(0)));
        //Model tmp = initial.cross_product(actions.get(2)).cross_product(actions.get(0));
        //System.out.println(tmp.cross_product(actions.get(4)));
        //System.out.println(tmp.cross_product(actions.get(4)).cross_product(actions.get(1)));
        //System.out.println(tmp.cross_product(actions.get(4)).cross_product(actions.get(1)).cross_product(actions.get(6)));
        //System.out.println(tmp.cross_product(actions.get(4)).cross_product(actions.get(1)).cross_product(actions.get(6)).cross_product(actions.get(8)));
        

        Search s = new Search(initial, goal, actions);
        return s;
    }
}

