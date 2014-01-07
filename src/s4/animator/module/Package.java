/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s4.animator.module;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

/**
 *
 * @author Dawson
 */
public class Package {
  String name;
  
  Node node;
  
  public Package(String name) {
    this.name = name;
  }
  
  //==Initializer
  //JavaFX
  public void initNode() {    
    StackPane packageBox = new StackPane();
    final Circle circle = new Circle(8, Color.ANTIQUEWHITE);
    final Label stateLabel = new Label(name);     
    stateLabel.setFont(Font.font("나눔고딕", 16));
    packageBox.getChildren().addAll(circle, stateLabel);
    
    node = packageBox;
  }
  
  //==Getter and Setter
  public Node getNode() {
    return node;
  }
}
