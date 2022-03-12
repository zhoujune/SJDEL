package formula;

import java.util.ArrayList;
import java.util.HashMap;

public class Event {
    public String name;
    public HashMap<String, ArrayList<Event>> relation = new HashMap<String, ArrayList<Event>>();
    
    public ArrayList<String> pre = new ArrayList<String>();
    public ArrayList<String> post = new ArrayList<String>();

    public Event(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setRelation(HashMap<String, ArrayList<Event>> relation){
        this.relation = relation;
    }

    public void addRelationByAgent(String agent, Event s){
        if(!relation.containsKey(agent)){
            relation.put(agent, new ArrayList<Event>());
        }
        relation.get(agent).add(s);
    }

    public ArrayList<Event> getAccessiblEventsByAgent(String agent){
        return relation.get(agent);
    }

    public void putPre(String info){
        if(info.contains("&")){
            for(String sub: info.split("&")){
                pre.add(sub.trim());
            }
        }
        else{
            pre.add(info.trim());
        }
    }

    public void putPost(String info){
        if(info.contains("&")){
            for(String sub: info.split("&")){
                post.add(sub.trim());
            }
        }
        else{
            post.add(info.trim());
        }
    }
}
