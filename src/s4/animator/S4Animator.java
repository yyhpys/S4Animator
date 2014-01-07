/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s4.animator;

import java.util.HashMap;
import java.util.Map.Entry;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import s4.animator.infos.Config;
import s4.animator.view.ControlView;
import s4.animator.view.AnimationView;
import s4.animator.module.Machine;
import s4.animator.module.Product;
import s4.animator.view.MenuView;
import s4.animator.controller.FileController;

/**
 *
 * @author Dawson
 */
public class S4Animator extends Application {
  private static String TITLE = "S4 Animator";
  
  private Stage stage;
  private static MainFrame mainFrame;

  public static MainFrame getMainFrame() {
    return mainFrame;
  }
  
  @Override
  public void start(Stage stage) {    
    VBox root = new VBox();
    mainFrame = new MainFrame(root);    
    mainFrame.initController();
    mainFrame.initView();
    
    this.stage = stage;
        
    Scene scene = new Scene(root);
    scene.widthProperty().addListener(new ChangeListener() {
      @Override
      public void changed(ObservableValue ov, Object t, Object t1) {
        //TODO: change panel size
      }
    });
    
    stage.setTitle(TITLE);
    stage.setResizable(true);
    stage.setScene(scene);
    
    stage.show();
    
    // close application
    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
      @Override
        public void handle(WindowEvent t) {
          Platform.exit();
          System.exit(0);
        }
      });
  }
  
  public class MainFrame {
    
    // Instance of scene root node
    private VBox root;
    
    //Controllers
    private FileController fileController;
    
    private ControlView controlView;
    private AnimationView animationView;
    private MenuView menuView;
    
    private HashMap<String, Machine> machines;
    private HashMap<String, Product> products;
 
    private MainFrame(VBox root) {
      this.root = root;
    }
    
    //== Getter and Setter
    //UI
    public void setTitle(String title) {
      stage.setTitle(TITLE + " - " + title);
    }
    
    //Control and View
    public FileController getFileController() {
      return fileController;
    }
    
    public ControlView getControlView() {
      return controlView;
    }
    
    public AnimationView getAnimationView() {
      return animationView;
    }
    
    public MenuView getMenuView() {
      return menuView;
    }
    
    //Machine and product
    public HashMap<String, Machine> getMachines() {
      if (machines == null)
        machines = new HashMap<String, Machine>();
      
      return machines;
    }
     
    public HashMap<String, Product> getProducts() {
      if (products == null)
        products = new HashMap<String, Product>();
      
      return products;
    }
    
    public void clearMachines() {
      if (machines != null)
        machines.clear();
      
      machines = null;
    }
    
    public void clearProducts() {
      if (products != null)
        products.clear();
      
      products = null;
    }
    
    //== Init
    public void initController() {
      fileController = new FileController();
      fileController.init();
    }
    
    public void initView() {
      //add menu
      menuView = new MenuView();
      menuView.init();
      
      //add view pane
      BorderPane borderPane = new BorderPane();
      
      controlView = new ControlView();
      controlView.init();
      controlView.setStyle("-fx-background-color: #bbbbbb;");
      controlView.setPadding(new Insets(10));
      //borderPane.setLeft(controlView);
      borderPane.setTop(controlView);
      
      animationView = new AnimationView();
      animationView.init();
      animationView.setMinWidth(Config.SCROLL_WIDTH);
      animationView.setMinHeight(Config.SCROLL_HEIGHT);
      animationView.setStyle("-fx-background-color: #eeeeee;");
      borderPane.setCenter(animationView);
      
      //add to root pane
      root.getChildren().addAll(menuView, borderPane);
    }
  }
  

  /**
   * The main() method is ignored in correctly deployed JavaFX application.
   * main() serves only as fallback in case the application can not be
   * launched through deployment artifacts, e.g., in IDEs with limited FX
   * support. NetBeans ignores main().
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}
