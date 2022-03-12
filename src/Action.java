package formula;
import java.util.ArrayList;

public class Action {
    public ArrayList<Event> events = new ArrayList<Event>();
    public ArrayList<Event> designaedEvents = events;
    public String name;

    public Action(ArrayList<Event> events){
        this.events = events;
    }
    public ArrayList<Event> get_events(){
        return events;
    }

}
