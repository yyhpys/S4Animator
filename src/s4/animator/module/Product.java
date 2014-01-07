/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s4.animator.module;

import java.util.HashMap;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import s4.animator.module.event.StateChangeEvent;
import s4.animator.infos.Config;
import s4.animator.infos.NodeColor;


/**
 *
 * @author Dawson
 */
public class Product {
  //== Constants
  //Product infos
  private String name;
  
  private int maxSequence = 0;
  private int[][] buffer;
  
  //Product Buffer state  
  public static final int EMPTY = 0;
  public static final int STOCK = 1;
  public static final int FULL = 2;
  
  public static final int[] STATES = new int[]{EMPTY, STOCK, FULL};
  
  //JavaFX node
  private HashMap<Integer, Pane> productNodes;
  
  private HashMap<Integer, Integer[]> productGrid;
  
  //==Constructor
  public Product (String productStr) {
    name = productStr;
  }
  
  //==Initializer
  //JavaFX
  public void initNodes() {
    //init maps
    productNodes = new HashMap<Integer, Pane>();
    
    //add nodes
    for (int i=0; i < getMaxSequence(); i++) {         
      for (int j=0; j < Machine.TYPES.length; j++) {       
        //Key as 10*sequence+Machine.type
        int key = 10*i + j;
                  
        //product node
        StackPane productBox = new StackPane();
        final Rectangle rect = new Rectangle(Config.BUFFER_WIDTH, Config.BUFFER_HEIGHT, NodeColor.PRODUCT[EMPTY]);
        final Label quantityLabel = new Label("0");     
        quantityLabel.setFont(Font.font("나눔고딕", 14));
        productBox.getChildren().addAll(rect, quantityLabel);
        
        VBox productGroup = new VBox();
        Label productLabel = new Label(name + "-" + i + "-" + Machine.TYPENAMES[j]);
        productGroup.setAlignment(Pos.CENTER);
        productGroup.setPadding(new Insets(10, 10, 10, 10));   
        productGroup.getChildren().addAll(productLabel, productBox);
        
        //attach state change handler
        productGroup.addEventHandler(StateChangeEvent.STATE_CHANGED, new EventHandler<StateChangeEvent>() {
          @Override
          public void handle(StateChangeEvent event) {        
            int curState = event.getState();
            rect.setFill(NodeColor.PRODUCT[curState]);
            quantityLabel.setText(event.getLabel());
          }
        });
          
        productNodes.put(key, productGroup);
      }
    }
  }
  
  //Product info
  public void initBuffer(int sequence) {
    if (sequence > maxSequence) {
      maxSequence = sequence;
      buffer = new int[sequence][Machine.TYPES.length];
    }
  }
  
  public void initState() {    
    for (int i=0; i < getMaxSequence(); i++)
      for (int j=0; j < Machine.TYPES.length; j++)
        buffer[i][j] = 0;
  }
  
  //==Getter and Setter
  //JavaFX
  public HashMap<Integer, Pane> getProductNodes() {
    return productNodes;
  }
  
  public void setProductGrid(int key, int[] grid) {
    if (productGrid == null)
      productGrid = new HashMap<Integer, Integer[]>();
    
    int i=0;
    Integer[] new_grid = new Integer[grid.length];
    for (int val : grid) new_grid[i++] = val;
    
    productGrid.put(key, new_grid);
  }
  
  public Integer[] getProductGrid(int key) {
    return productGrid.get(key);
  }
  
  //Product info
  public int getMaxSequence() {
    return maxSequence;
  }
  
  public int[][] getBuffer() {
    return buffer;
  }
  
  //==Method
  //JavaFX  
  public void updateNode() {
    for (int i=0; i < getMaxSequence(); i++) {
      for (int j=0; j < Machine.TYPES.length; j++) { 
        int nodeId = 10*i + j;
        Node node = getProductNodes().get(nodeId);
        
        int bufferSize = buffer[i][j];
        if (bufferSize == 0)
          node.fireEvent(new StateChangeEvent(StateChangeEvent.STATE_CHANGED, EMPTY, String.valueOf(bufferSize)));
        else if (bufferSize > 0)
          node.fireEvent(new StateChangeEvent(StateChangeEvent.STATE_CHANGED, STOCK, String.valueOf(bufferSize)));
        //else if (bufferSize == FULL)
        //  getNode().fireEvent(new StateChangeEvent(StateChangeEvent.STATE_RUN, FULL, ));
      }
    }      
  }
            
  //buffer
  public void addBuffer(int sequence, int type, int addSize) {
    buffer[sequence][type] += addSize;
  }
  
  public void removeBuffer(int sequence, int type, int removeSize) {
    buffer[sequence][type] -= removeSize;    
  }
}
