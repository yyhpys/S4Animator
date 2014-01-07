/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s4.animator.view.task;

import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.concurrent.Task;
import s4.animator.S4Animator;
import s4.animator.view.ControlView;
import s4.animator.view.AnimationView;
import s4.animator.module.Machine;
import s4.animator.module.Product;
import s4.animator.view.dialog.Dialog;

/**
 *
 * @author Dawson
 */
public class RunAnimator extends Task<Void> {
  
  AnimationView animationView;
  ControlView controlView;
  
  public RunAnimator(AnimationView animationView, ControlView controlView) {
    this.animationView = animationView;
    this.controlView = controlView;
  }
  
  @Override
  public Void call() {
    int curTime = animationView.getTime();
    int curState = animationView.getState();
    
    while (curState == AnimationView.RUN) {
      System.out.println("run - " + curTime);
          
      boolean runDone = true;
      
      int utilization = 0;
      int rundown = 0;

      //update machines
      HashMap<String, Machine> machines = S4Animator.getMainFrame().getMachines(); 
      for (Map.Entry<String, Machine> entry : machines.entrySet()) {
        final Machine curMachine = entry.getValue();        
        curMachine.run(curTime);
        if (!curMachine.isScheduleEmpty())
          runDone = false;
            
        //update machines
        updateMachine(curMachine);
        
        //get target result value
        utilization += curMachine.getUtilizedTime();
        rundown += curMachine.getRundownTime();
      }
          
      //update products
      HashMap<String, Product> products = S4Animator.getMainFrame().getProducts(); 
      for (Map.Entry<String, Product> entry : products.entrySet()) {
        final Product curProduct = entry.getValue();
        
        //update products
        updateProduct(curProduct);
      }

      //increment unit time
      updateTime(curTime);          
      animationView.setTime(curTime+1);
          
      //stop if there is no schedule
      if (runDone) {
        animationView.setState(AnimationView.STOP);   
        break;
      }
        
      try {
        int sleepTime = S4Animator.getMainFrame().getControlView().getTimeSleep();
        Thread.sleep(sleepTime);
      } catch (InterruptedException ex) {
        System.out.println("Sleep Interrupted..");
      } finally {
        if (isCancelled())
          animationView.setState(AnimationView.PAUSE);
      }
      
      //update target result indicator      
      double total_utilization = (double) utilization / (machines.size() * (curTime+1));
      double total_rundown = (double) rundown / (machines.size() * (curTime+1));
      controlView.setUtilizationProgress(total_utilization);
      controlView.setRundownProgress(total_rundown);
      //updateProgress(total_utilization, total_rundown);
      
      //update local variable
      curTime = animationView.getTime();
      curState = animationView.getState();
    }
    
    //if run done, print dialog with result
    Dialog.showInfo("Simulation Complete", "Total Time: " + animationView.getTime());

    return null;
  }
  
  
  //UI Progress
  public void updateMachine (final Machine machine) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {                              
        machine.updateNode();
      }
    });
  }
  
  public void updateProduct (final Product product) {
    Platform.runLater (new Runnable() {
      @Override
      public void run() {                              
        product.updateNode();
      }
    });
  }
  
  public void updateTime (final int time) {
    Platform.runLater (new Runnable() {
        @Override
        public void run() {              
          controlView.setTimeLabel(time);
        }
    });
  }

  public void updateProgress(final double util, final double rundown) {
    Platform.runLater (new Runnable() {
        @Override
        public void run() {              
          //controlView.setTimeLabel(time);
          controlView.setUtilizationProgress(util);
          controlView.setRundownProgress(rundown);
        }
    });
  }
}
