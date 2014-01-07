/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s4.animator.module.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

/**
 *
 * @author Dawson
 */
public class StateChangeEvent extends Event {    
    public static final EventType<StateChangeEvent> STATE_CHANGED = new EventType(ANY);
    public static final EventType<StateChangeEvent> STATE_IDLE = new EventType(STATE_CHANGED, "STATE_IDLE");
    public static final EventType<StateChangeEvent> STATE_SETUP = new EventType(STATE_CHANGED, "STATE_SETUP");
    public static final EventType<StateChangeEvent> STATE_RUN = new EventType(STATE_CHANGED, "STATE_RUN");
    
    private int state;
    private String label;
    
    public StateChangeEvent(EventType<? extends Event> arg0) {
      super(arg0);
    }
    
    public StateChangeEvent(EventType<? extends Event> arg0, int state) {
      super(arg0);
      this.state = state;
      this.label = null;
    }    
    
    public StateChangeEvent(EventType<? extends Event> arg0, int state, String label) {
      super(arg0);
      this.state = state;
      this.label = label;
    }
    
    public StateChangeEvent(Object arg0, EventTarget arg1, EventType<? extends Event> arg2) {
      super(arg0, arg1, arg2);
    }
    
    //==Getter and Setter
    public int getState() {
      return state;
    }
    
    public String getLabel() {
      return label;
    }
}
