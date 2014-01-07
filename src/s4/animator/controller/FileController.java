/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s4.animator.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import s4.animator.S4Animator;
import s4.animator.module.Machine;
import s4.animator.module.Product;
import s4.animator.module.Schedule;
import s4.animator.utils.Utils;
import s4.animator.view.AnimationView;

/**
 *
 * @author Dawson
 */
public class FileController extends VBox {    
  // Input File
  private File file;
  
  // State
  private boolean fileReady;
    
  public void init() {
    file = null;
    fileReady = false;
  }
  
  //== Getter and Setter
  public void setFile(File file) {
    this.file = file;
  }
  
  public void setFileState(boolean fileReady) {
    this.fileReady = fileReady;
  }
  
  public File getFile() {
    return file;
  }
  
  public boolean isFileReady() {
    return fileReady;
  }
  
  
  //== Methods
  public boolean parseFile(File inputFile) {
    if (inputFile == null) {
      fileReady = false;
      return false;
    }
    
    S4Animator.getMainFrame().clearMachines();
    S4Animator.getMainFrame().clearProducts();
    HashMap<String, Machine> machines = S4Animator.getMainFrame().getMachines();
    HashMap<String, Product> products = S4Animator.getMainFrame().getProducts();
    
    try {
      BufferedReader br = new BufferedReader(new FileReader(inputFile));      
      String line = br.readLine();      
      
      long baseTimestamp = -1;
      
      while ((line = br.readLine()) != null) {
        String[] inputs = line.split("\t");
        
        //Add machine
        String machineStr = inputs[3];
        if (!machines.containsKey(machineStr)) {
          Machine curMachine = new Machine(machineStr);     
          machines.put(machineStr, curMachine);
        }
        Machine curMachine = machines.get(machineStr);
        //curMachine.initState();
        
        //Add product
        int sequence = Integer.parseInt(inputs[1]);
        String productStr = inputs[2];
        if (!products.containsKey(productStr)) {
          Product curProduct = new Product(productStr);     
          products.put(productStr, curProduct);
        }
        
        Product curProduct = products.get(productStr);
        curProduct.initBuffer(sequence);
        
        //Add schedule to machine
        int quantity = Integer.parseInt(inputs[4]);
        //long startTime = Long.parseLong(inputs[5]);
        //long endTime = Long.parseLong(inputs[6]);
        
        long startTimestamp = Utils.convertDateToTimestamp(inputs[5]);
        long endTimestamp = Utils.convertDateToTimestamp(inputs[6]);
        
        if (baseTimestamp < 0)
          baseTimestamp = startTimestamp;
        
        long startTime = (startTimestamp - baseTimestamp) / 60L;
        long endTime = (endTimestamp - baseTimestamp) / 60L;
        
        int setupTime = Integer.parseInt(inputs[7]);
        
        Schedule curSchedule = new Schedule(machineStr, productStr);
        curSchedule.setSequence(sequence-1);
        curSchedule.setQuantity(quantity);
        curSchedule.setSetupTime(setupTime);
        curSchedule.setStartTime(startTime);
        curSchedule.setEndTime(endTime);
        
        curMachine.addSchedule(curSchedule);
      }      
    } catch (Exception e) {
      e.printStackTrace();
      fileReady = false;
      S4Animator.getMainFrame().clearMachines();
      S4Animator.getMainFrame().clearProducts();
      
      return false;
    }
    
    fileReady = true;
    return true;
  }
}
