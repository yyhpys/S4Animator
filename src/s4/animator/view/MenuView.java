/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s4.animator.view;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import s4.animator.S4Animator;
import s4.animator.controller.FileController;
import s4.animator.view.dialog.Dialog;

/**
 *
 * @author Dawson
 */
public class MenuView extends MenuBar {
  //== Initialize
  public void init() {
    //file menu
    Menu menuFile = new Menu("File");
    
    MenuItem menuitemOpen = new MenuItem("Open..");
    menuitemOpen.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        FileController fileController = S4Animator.getMainFrame().getFileController();
        FileChooser fileChooser = new FileChooser();
 
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("S4 Schedule file (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
             
        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);     
        System.out.println(file);
        
        //Parse input text
        if (file != null)     
          fileController.parseFile(file);
        else
          return;
        
        if(fileController.isFileReady()) {
          S4Animator.getMainFrame().setTitle(file.getPath());
          
          System.out.println("Change State : STOP");
          S4Animator.getMainFrame().getAnimationView().setState(AnimationView.STOP);
        } else {
          Dialog.showError("파일 에러", "올바른 스케쥴 파일이 아닙니다");
        }
      }
    });       
    MenuItem menuitemExit = new MenuItem("Exit");
    menuitemExit.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        System.exit(0);
      }
    });
    
    menuFile.getItems().addAll(menuitemOpen, menuitemExit);
    
    //help menu
    Menu menuHelp = new Menu("Help");
    
    MenuItem menuitemAbout = new MenuItem("About..");
    menuitemAbout.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        Dialog.showInfo("S4 Animator", "Animator for S4 Simulator\n\nBy Information Management Lab\nJoo-shik Yoon");
      }
    });
            
    menuHelp.getItems().addAll(menuitemAbout);
    
    //add to menus
    getMenus().addAll(menuFile, menuHelp);
  }
}
