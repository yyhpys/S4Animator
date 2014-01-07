/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s4.animator.infos;

import javafx.scene.paint.Color;
import s4.animator.module.Machine;
import s4.animator.module.Product;

/**
 *
 * @author Dawson
 */
public class NodeColor {
  public static Color[] MACHINE = createMachineStateArray();  
  
  private static Color[] createMachineStateArray() {
    Color[] colors = new Color[Machine.STATES.length];
    
    colors[Machine.IDLE] = Color.GRAY;
    colors[Machine.SETUP] = Color.YELLOW;
    colors[Machine.RUN] = Color.LIGHTGREEN;
    colors[Machine.MAINTENANCE] = Color.RED;
    
    return colors;
  }
  
  public static Color[] PRODUCT = createProductStateArray();  
  
  private static Color[] createProductStateArray() {
    Color[] colors = new Color[Product.STATES.length];
    
    colors[Product.EMPTY] = Color.GRAY;
    colors[Product.STOCK] = Color.YELLOW;
    colors[Product.FULL] = Color.RED;
    
    return colors;
  }
}
