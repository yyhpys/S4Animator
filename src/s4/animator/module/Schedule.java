/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s4.animator.module;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;
import s4.animator.S4Animator;

/**
 *
 * @author Dawson
 */
public class Schedule {
  //==Constants
  //Schedule info
  private String name;
  private String machine;
  
  private int quantity;
  private int sequence;
  
  private int setupTime;
  private long startTime;
  private long endTime;  
  
  //==Constructor
  public Schedule(String machineStr, String productStr) {
    name = productStr;
    machine = machineStr;
  }
  
  //==Getter and Setter
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
  
  public void setSequence(int sequence) {
    this.sequence = sequence;
  }
    
  public void setSetupTime(int setupTime) {
    this.setupTime = setupTime;
  }
  
  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }
  
  public void setEndTime(long endTime) {
    this.endTime = endTime;
  }
  
  public String getName() {
    return name;
  }
  
  public String getMachineName() {
    return machine;
  }
  
  public int getQuantity() {
    return quantity;
  }
  
  public int getSequence() {
    return sequence;
  }
  
  public long getSetupTime() {
    return setupTime;
  }
  
  public long getStartTime() {
    return startTime;
  }
  
  public long getEndTime() {
    return endTime;
  }
  
  //==Method
  public void getQuantityFromBuffer() {
    Product curProduct = S4Animator.getMainFrame().getProducts().get(getName());
    Machine curMachine = S4Animator.getMainFrame().getMachines().get(getMachineName());
    
    //temp number control
    if (curMachine.getMachineType() == 0)
      return;
    
    curProduct.removeBuffer(getSequence(), curMachine.getMachineType()-1, getQuantity()); 
    if ((curMachine.getMachineType() == Machine.DIEATTACH) && (curMachine.currentSchedule().sequence > 0))
      curProduct.removeBuffer(getSequence()-1, Machine.TYPES.length-1, getQuantity());    
    
    //temp animation
    showRemoveBufferAnimation(10*getSequence() + curMachine.getMachineType()-1);    
    if ((curMachine.getMachineType() == Machine.DIEATTACH) && (curMachine.currentSchedule().sequence > 0))
      showRemoveBufferAnimation(10*(getSequence()-1) + Machine.TYPES.length-1);
  }
  
  public void addQuantityToBuffer() {
    Product curProduct = S4Animator.getMainFrame().getProducts().get(getName());
    Machine curMachine = S4Animator.getMainFrame().getMachines().get(getMachineName());
    
    curProduct.addBuffer(getSequence(), curMachine.getMachineType(), getQuantity());
    
    
    //temp animation
    showAddBufferAnimation();
  }
  
  
  
  //temp animation
  public void showAddBufferAnimation() {
    Platform.runLater (new Runnable() {
      @Override
      public void run() {   
        Product curProduct = S4Animator.getMainFrame().getProducts().get(getName());
        Machine curMachine = S4Animator.getMainFrame().getMachines().get(getMachineName());
        
        //get machine and product node        
        Node machineNode = curMachine.getNode();
        double machineX = machineNode.getScene().getWindow().getX() + machineNode.getScene().getX() + machineNode.localToScene(0, 0).getX();
        double machineY = machineNode.getScene().getWindow().getY() + machineNode.getScene().getY() + machineNode.localToScene(0, 0).getY();
        
        int machineGridX = curMachine.getGrid()[0];
        int machineGridY = curMachine.getGrid()[1];
        
        Node productNode = curProduct.getProductNodes().get(10*getSequence() + curMachine.getMachineType());
        double productX = productNode.getScene().getWindow().getX() + productNode.getScene().getX() + productNode.localToScene(0, 0).getX();
        double productY = productNode.getScene().getWindow().getY() + productNode.getScene().getY() + productNode.localToScene(0, 0).getY();
                
        //init package
        Package pathPackage = new Package(getName());
        pathPackage.initNode();
        
        final Node packageNode = pathPackage.getNode();
        S4Animator.getMainFrame().getAnimationView().getNode().add(packageNode, machineGridX, machineGridY);
        
        

        //start timeline
        KeyValue kvX = new KeyValue(packageNode.translateXProperty(), productX - machineX);
        KeyValue kvY = new KeyValue(packageNode.translateYProperty(), productY - machineY);
        
        Duration duration = Duration.millis(500);
        EventHandler onFinished = new EventHandler<ActionEvent>() {
          public void handle(ActionEvent t) {
            S4Animator.getMainFrame().getAnimationView().getNode().getChildren().remove(packageNode);
          }
        };
        KeyFrame kf = new KeyFrame(duration, onFinished , kvX, kvY);
        
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(kf);
        timeline.play();
      }
    });
  }
  
  //temp animation
  public void showRemoveBufferAnimation(final int previousProductKey) {
    Platform.runLater (new Runnable() {
      @Override
      public void run() {   
        Product curProduct = S4Animator.getMainFrame().getProducts().get(getName());
        Machine curMachine = S4Animator.getMainFrame().getMachines().get(getMachineName());
        
        //get machine and product node        
        Node machineNode = curMachine.getNode();
        double machineX = machineNode.getScene().getWindow().getX() + machineNode.getScene().getX() + machineNode.localToScene(0, 0).getX();
        double machineY = machineNode.getScene().getWindow().getY() + machineNode.getScene().getY() + machineNode.localToScene(0, 0).getY();
        
        //normal buffer removal         
        Node productNode = curProduct.getProductNodes().get(previousProductKey);
        double productX = productNode.getScene().getWindow().getX() + productNode.getScene().getX() + productNode.localToScene(0, 0).getX();
        double productY = productNode.getScene().getWindow().getY() + productNode.getScene().getY() + productNode.localToScene(0, 0).getY();
                
        int productGridX = curProduct.getProductGrid(previousProductKey)[0];
        int productGridY = curProduct.getProductGrid(previousProductKey)[1];        
        
        //init package
        Package pathPackage = new Package(getName());
        pathPackage.initNode();
        
        final Node packageNode = pathPackage.getNode();
        S4Animator.getMainFrame().getAnimationView().getNode().add(packageNode, productGridX, productGridY);
        

        //start timeline
        KeyValue kvX = new KeyValue(packageNode.translateXProperty(), machineX - productX);
        KeyValue kvY = new KeyValue(packageNode.translateYProperty(), machineY - productY);
        
        Duration duration = Duration.millis(500);
        EventHandler onFinished = new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent t) {
            S4Animator.getMainFrame().getAnimationView().getNode().getChildren().remove(packageNode);
          }
        };
        KeyFrame kf = new KeyFrame(duration, onFinished , kvX, kvY);
        
        Timeline timeline = new Timeline();
        timeline.setDelay(duration);
        timeline.getKeyFrames().add(kf);
        timeline.play();
      }
    });
  }
  
  
}
