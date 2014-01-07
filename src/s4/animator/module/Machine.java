/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s4.animator.module;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import s4.animator.S4Animator;
import s4.animator.module.event.StateChangeEvent;
import s4.animator.infos.Config;
import s4.animator.infos.NodeColor;
import s4.animator.view.dialog.Dialog;

/**
 *
 * @author Dawson
 */
public class Machine {
  //== Constants
  //Machine infos
  public static final int BACKLAB = 0;
  public static final int SAWING = 1;
  public static final int DIEATTACH = 2;
  public static final int WIREBONDING = 3;
  
  public static final int[] TYPES = {BACKLAB, SAWING, DIEATTACH, WIREBONDING};
  public static final String[] TYPENAMES = {"BL", "SW", "DA", "WB"};
  
  private int type;
  private int number;
  
  private ArrayList<Schedule> schedules;  
  
  //Machine state  
  public static final int IDLE = 0;
  public static final int SETUP = 1;
  public static final int RUN = 2;
  public static final int MAINTENANCE = 3;
  
  public static final int[] STATES = new int[]{IDLE, SETUP, RUN, MAINTENANCE};
  
  private int state;
  private int index;
  
  //Target result
  private int rundown;
  private int utilized;
  
  //JavaFX node
  private Pane node;
  
  private int[] grid;
  
  //==Constructor
  public Machine (String machineStr) {
    String machine_type = machineStr.substring(0, 2); 
    setMachineType(machine_type);
      
    int machine_no = Integer.parseInt(machineStr.substring(2));
    setMachineNumber(machine_no);
  }
    
  //==Initializer
  //JavaFX
  public void initNode() {    
    StackPane machineBox = new StackPane();
    final Rectangle rect = new Rectangle(Config.MACHINE_WIDTH, Config.MACHINE_HEIGHT, NodeColor.MACHINE[IDLE]);
    final Label stateLabel = new Label(null);     
    stateLabel.setFont(Font.font("나눔고딕", 16));
    machineBox.getChildren().addAll(rect, stateLabel);
    
    VBox machineGroup = new VBox();
    Label machineLabel = new Label(Machine.TYPENAMES[type] + "-" + number);
    machineGroup.setAlignment(Pos.CENTER);
    machineGroup.setPadding(new Insets(10, 30, 10, 30));        
    machineGroup.getChildren().addAll(machineLabel, machineBox);
    
    final Tooltip machineTooltip = new Tooltip();
    machineTooltip.setText("wow");
    machineGroup.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent t) {
        int u = (int) (getUtilization() * 100);
        int r = (int) (getRundown() * 100);
        machineTooltip.setText("Type: " + Machine.TYPENAMES[getMachineType()] + "\nUtilization: " + u + "%\nRundown: " + r + "%");
        
        double x =  rect.getScene().getWindow().getX() + rect.getScene().getX() + rect.localToScene(0, 0).getX()+50;
        double y =  rect.getScene().getWindow().getY() + rect.getScene().getY() + rect.localToScene(0, 0).getY()+50;        
        machineTooltip.show(rect, x, y);
      }
    });
    machineGroup.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent t) {
        machineTooltip.hide();
      }
    });
    machineGroup.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent t) {
        Dialog.showWarning("Machine Information", "Under Construction");
      }
    });

    node = machineGroup;    
    //attach state change handler
    node.addEventHandler(StateChangeEvent.STATE_CHANGED, new EventHandler<StateChangeEvent>() {
      @Override
      public void handle(StateChangeEvent event) {        
        int curState = event.getState();
        rect.setFill(NodeColor.MACHINE[curState]);
        stateLabel.setText(event.getLabel());
      }
    });
    
  }
  
  
  
  //Schedule
  public void initState() {
    state = IDLE;
    index = 0;
    utilized = 0;
  }
  
  
  //==Getter and Setter
  //JavaFX
  public Pane getNode() {
    return node;
  }
  
  public void setGrid(int[] grid) {
    this.grid = grid;
  }
  
  public int[] getGrid() {
    return grid;
  }
  
  //Machine infos
  public void setMachineType(String machine_type) {
    for (int i=0; i < TYPENAMES.length; i++) {
      if (machine_type.equalsIgnoreCase(TYPENAMES[i]))
        type = TYPES[i];
    }
  }
  
  public void setMachineNumber(int machine_no) {
    number = machine_no;
  }
  
  
  public int getMachineType() {
    return type;
  }
  
  public int getMachineNumber() {
    return number;
  }
  
  //Schedule
  public void addSchedule(Schedule sch) {
    if (schedules == null)
      schedules = new ArrayList<Schedule>();
    
    schedules.add(sch);
  }
  
  public void moveSchedule() {
    //schedules.remove(0);
    index++;
  }
  
  public boolean isScheduleEmpty() {
    if (schedules.size() > index)
      return false;
    else
      return true;
  }
  
  public Schedule currentSchedule() {
    if (schedules.size() > index)
      return schedules.get(index);
    else
      return null;
  }
  
  //target res
  public int getUtilizedTime() {
    return utilized;
  }
  
  public int getRundownTime() {
    return rundown;
  }
  
  public double getUtilization() {
    return (double) getUtilizedTime() / S4Animator.getMainFrame().getAnimationView().getTime();
  }
  
  public double getRundown() {
    return (double) getRundownTime() / S4Animator.getMainFrame().getAnimationView().getTime();
  }
  
  //=== Method
  //JavaFX  
  public void updateNode() {
    if (state == IDLE)
      getNode().fireEvent(new StateChangeEvent(StateChangeEvent.STATE_IDLE, IDLE, ""));
    else if (state == SETUP)
      getNode().fireEvent(new StateChangeEvent(StateChangeEvent.STATE_SETUP, SETUP, currentSchedule().getName()));
    else if (state == RUN)
      getNode().fireEvent(new StateChangeEvent(StateChangeEvent.STATE_RUN, RUN, currentSchedule().getName()));
  }
  
  //Schedule
  public void toIdle() {
    state = IDLE;
  }
  
  public void toSetup() {
    state = SETUP;
  }
  
  public void toRun() {
    state = RUN;
    
    //fetch from buffer
    currentSchedule().getQuantityFromBuffer();
  }
  
  public void toMaintenance() {
    state = MAINTENANCE;
  }
  
  public void incTime() {
    if (state == IDLE) {
      rundown++;
    } else if (state == SETUP) {
    } else if (state == RUN) {   
      utilized++;
    } else if (state == MAINTENANCE) {
      //TODO: check maintenance state
    }
  }
  
  public void run(int time) {
    incTime();
    
    //if there is no schedule - return;
    Schedule curSchedule = currentSchedule();
    if (curSchedule == null)
      return;
    
        //or do schedule
    if (state == IDLE) {
      if (curSchedule.getStartTime() == time) {
        //start schedule
        toRun();
      }
    } else if (state == SETUP) {
      if ((curSchedule.getEndTime() + curSchedule.getSetupTime()) == time) {
        //check if setup is done
        
        //move to next schedule
        moveSchedule();
        Schedule nextSchedule = currentSchedule();
        
        if (nextSchedule.getStartTime() == time) {
          //start schedule
          toRun();
        } else {
          //idle state
          toIdle();
        }
      }
    } else if (state == RUN) {
      if (curSchedule.getEndTime() == time) {
        //add to buffer
        curSchedule.addQuantityToBuffer();
        
        if (curSchedule.getSetupTime() > 0) {
          toSetup();
        } else {
          //move to next schedule
          moveSchedule();
          Schedule nextSchedule = currentSchedule();
          
          if (nextSchedule == null) {
            toIdle();
          } else if (nextSchedule.getStartTime() == time) {
            //start schedule
            toRun();
          } else {
            //idle state
            toIdle();
          }
        }
      }
    } else if (state == MAINTENANCE) {
      //TODO: check maintenance state
    }
    
    /* BACKUP (setup time problem)
    //or do schedule
    if (state == IDLE) {
      if (curSchedule.getStartTime() == time) {
        //schedule start
        if (curSchedule.getSetupTime() > 0) {          
          //check if setup time exist
          toSetup();
        } else {
          //start schedule
          toRun();
        }
      }
    } else if (state == SETUP) {
      if ((curSchedule.getStartTime() + curSchedule.getSetupTime()) == time) {
        //check if setup is done
        toRun();
      }
    } else if (state == RUN) {
      if (curSchedule.getEndTime() == time) {
        //add to buffer
        curSchedule.addQuantityToBuffer();
        
        //move to next schedule
        moveSchedule();
        Schedule nextSchedule = currentSchedule();
        
        if (nextSchedule == null) {
          toIdle();
        } else if (nextSchedule.getStartTime() == time) {
          //schedule start
          if (nextSchedule.getSetupTime() > 0) {          
            //check if setup time exist
            toSetup();
          } else {
            //start schedule
            toRun();
          }
        } else {
          //idle state
          toIdle();
        }
      }
    } else if (state == MAINTENANCE) {
      //TODO: check maintenance state
    }
    */
  }
  
}

