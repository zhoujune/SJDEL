package formula;

import java.util.ArrayList;

public class Search {
    Model start = null;
    Formula goal = null;
    ArrayList<Action> actions = new ArrayList<Action>();
    boolean detail = false;
    public Search(Model start, Formula goal, ArrayList<Action> actions){
        this.start = start;
        this.goal = goal;
        this.actions = actions;
    }

    public void BFS(){
        
        ArrayList<Model> visited = new ArrayList<Model>();
        ArrayList<Model> frontier = new ArrayList<Model>();
        frontier.add(this.start);
        long startTime = System.currentTimeMillis();
        long longest_wait_time = 100000;

        while(frontier.size() != 0 & System.currentTimeMillis()-startTime < longest_wait_time){

            Model current = frontier.remove(0);
            visited.add(current);
            if(current.models(goal)){
                System.out.println("Solution found");
                print_solution(current);
                return;
            }
            ArrayList<Model> products = current.get_all_cross_product_models(actions);
            for(Model p: products){
                if(!visited.contains(p)){
                    frontier.add(p);
                }
            }
            if(detail){
                print_details(current, products);
            }
        }

        if(frontier.size() != 0){
            System.out.println("Exceeded time limit");
        }
        else{
            System.out.println("No solution");
        }

    }

    public static void print_solution(Model sol){
        ArrayList<Model> parent_models = new ArrayList<Model>();
        ArrayList<Action> parent_actions = new ArrayList<Action>();
        Model tmp = sol;
        while(sol.parent_model != null){
            parent_models.add(sol.parent_model);
            parent_actions.add(sol.parent_action);
            sol = sol.parent_model;
        }
        int step = 0;
        for(int i = parent_models.size()-1; i >= 0; i--){
            
            System.out.println("Step "+ (parent_models.size()-i) +": take action "+parent_actions.get(i).name+", resulting model: ");
            if(i == 0){
                System.out.println(tmp);
                System.out.println("That is the goal state");
            }
            else{
                System.out.println(parent_models.get(i-1));
            }
        }

        //System.out.println("----");
        //System.out.println(tmp.cross_product(a.get(8)).cross_product(a.get(4)));
        //System.out.println(tmp.cross_product(a.get(8)).cross_product(a.get(4)).cross_product(a.get(9)));
        //.cross_product(a.get(4)).cross_product(a.get(9)).cross_product(a.get(6))
    }

    public void print_details(Model current, ArrayList<Model> products){
        System.out.println("Exploring state: \n"+ current + ", not the goal state, "+ products.size()+" childrens are added to the frontier:");
        for(Model p: products){
            System.out.println("With action "+p.parent_action.name+ ", extend state\n"+p);
        }
    }


}
