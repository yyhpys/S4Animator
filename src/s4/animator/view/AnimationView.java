/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s4.animator.view;

import java.util.HashMap;
import java.util.Map.Entry;
import javafx.animation.Timeline;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import s4.animator.S4Animator;
import s4.animator.view.task.RunAnimator;
import s4.animator.module.Machine;
import s4.animator.module.Product;

/**
 *
 * @author Dawson
 */
public class AnimationView extends ScrollPane {
  //public static final int INIT = 0;
  public static final int STOP = 1;
  public static final int PAUSE = 2;
  public static final int RUN = 3;
  
  public static final int GRID_GAP = 2;
  
  
  private int curTime;
  private int curState;
  
  private RunAnimator runAnimator;
  
  //Grid Panel
  private GridPane machinePane;
  
  
  public void init() {
    setPannable(true);
  }
  
  //== Getter and Setter
  public void setTime(int time) {
    curTime = time;
  }
  
  public void setState(int state) {
    curState = state;
    
    if (state == STOP)
      handleStop();
    else if (state == PAUSE)
      handlePause();
    else if (state == RUN)
      handleRun();
  }
  
  public int getTime() {
    return curTime;
  }
  
  public int getState() {
    return curState;
  }
  
  public GridPane getNode() {
    return machinePane;
  }
  
  
  
  //==method
  public void initGrid() {
    machinePane = new GridPane();
    setContent(machinePane);
    
    
    HashMap<String, Product> products = S4Animator.getMainFrame().getProducts();      
        
    //Init product grid index
    int productGridIndex = 0;
    
    //Batch product to grid
    for (Entry<String, Product> entry : products.entrySet()) {
      Product curProduct = entry.getValue();      
      curProduct.initNodes();
      curProduct.initState();
      
      for (int i=0; i < curProduct.getMaxSequence(); i++) {
        for (int j=0; j < Machine.TYPES.length; j++) {       
          //add product buffer
          int[] grid = new int[]{2*j+1, productGridIndex};   
          
          int nodeKey = 10*i + j;          
          Pane productGroup = curProduct.getProductNodes().get(nodeKey); 
          curProduct.setProductGrid(nodeKey, grid);
          
          machinePane.add(productGroup, grid[0], grid[1]);
        }
        
        productGridIndex++;
      }
    }
    
    //Init machine grid index
    int[] machineGridIndex = new int[Machine.TYPES.length];   
    for (int i=0; i < machineGridIndex.length; i++)
      machineGridIndex[i] = 0;
      
    //Batch machines to grid
    HashMap<String, Machine> machines = S4Animator.getMainFrame().getMachines();
    for (Entry<String, Machine> entry : machines.entrySet()) {
      Machine curMachine = entry.getValue();
      curMachine.initNode();
      curMachine.initState();
      
      int[] grid = {2*curMachine.getMachineType(), machineGridIndex[curMachine.getMachineType()]++};         
      Pane machineGroup = curMachine.getNode();
      curMachine.setGrid(grid);
        
      machinePane.add(machineGroup, grid[0], grid[1]);
    }
    
    //reset time
    curTime = 0;
    S4Animator.getMainFrame().getControlView().setTimeLabel(curTime);
    
    //reset indicator
    S4Animator.getMainFrame().getControlView().setUtilizationProgress(0);
    S4Animator.getMainFrame().getControlView().setRundownProgress(0);
  }
  
  //Handle state
  public void handleStop() {
    initGrid();
  }
  
  public void handlePause() {    
    runAnimator.cancel();
  }
  
  public void handleRun() {   
    runAnimator = new RunAnimator(this, S4Animator.getMainFrame().getControlView());      
    new Thread(runAnimator).start();      
  }
  
}
