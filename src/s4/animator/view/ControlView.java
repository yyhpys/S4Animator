/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s4.animator.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import s4.animator.S4Animator;

/**
 *
 * @author Dawson
 */
public class ControlView extends HBox {
  
  Label timeLabel;
  Slider timeSpeedSlider;
  
  ProgressIndicator utilizationIndicator;
  ProgressIndicator rundownIndicator;
  
  
  private static int BASE_TIME = 1000;
  
  //== Initialize
  public void init() {
    //init style
    this.setSpacing(30);
    
    //logo
    ImageView logoView = new ImageView();
    logoView.setImage(new Image("/s4/animator/view/resource/logo.png"));
    
    //Start and Pause button
    HBox buttonGroup = new HBox();
    
    Button startBtn = new Button();
    startBtn.setText("▶");
    startBtn.setFont(Font.font("나눔고딕", 14));
    startBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent t) {
        if(!S4Animator.getMainFrame().getFileController().isFileReady())
          return;
          
        System.out.println("Change State : START");
        S4Animator.getMainFrame().getAnimationView().setState(AnimationView.RUN);
      }
    });
    buttonGroup.getChildren().add(startBtn);
    
    Button pauseBtn = new Button();
    pauseBtn.setText("||");
    pauseBtn.setFont(Font.font("나눔고딕", 14));
    pauseBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent t) {
        System.out.println("Change State : PAUSE");
        S4Animator.getMainFrame().getAnimationView().setState(AnimationView.PAUSE);
      }
    });
    buttonGroup.getChildren().add(pauseBtn);
    
    Button stopBtn = new Button();
    stopBtn.setText("■");
    stopBtn.setFont(Font.font("나눔고딕", 14));
    stopBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent t) {
        System.out.println("Change State : STOP");
        S4Animator.getMainFrame().getAnimationView().setState(AnimationView.STOP);
      }
    });
    buttonGroup.getChildren().add(stopBtn);
    buttonGroup.setAlignment(Pos.CENTER);
    buttonGroup.setPadding(new Insets(10, 20, 10, 20));
    buttonGroup.setStyle("-fx-border-color: gray; =-fx-border-width: 1; -fx-border-style: dotted;");
    
    //time box
    HBox timeBox = new HBox();
    timeSpeedSlider = new Slider(0, 980, 500);
    timeSpeedSlider.setTooltip(new Tooltip("속도"));
    timeBox.getChildren().add(timeSpeedSlider);
    
    timeLabel = new Label();
    timeLabel.setFont(Font.font("나눔고딕", FontWeight.BOLD, 20));
    timeBox.getChildren().add(timeLabel);
    timeBox.setAlignment(Pos.CENTER);
    timeBox.setPadding(new Insets(10, 20, 10, 20));
    timeBox.setStyle("-fx-border-color: gray; =-fx-border-width: 1; -fx-border-style: dotted;");
    
    //target percentage label
    HBox percentageBox = new HBox();
    
    VBox utilizationBox = new VBox();
    utilizationBox.setAlignment(Pos.CENTER);
    Label utilizationLabel = new Label("Utilization");
    utilizationIndicator = new ProgressIndicator();
    utilizationIndicator.setProgress(0);
    utilizationIndicator.setMinWidth(70);
    utilizationIndicator.setMinHeight(70);
    utilizationBox.getChildren().addAll(utilizationLabel, utilizationIndicator);
    
    VBox rundownBox = new VBox();
    rundownBox.setAlignment(Pos.CENTER);
    Label rundownLabel = new Label("Rundown");
    rundownIndicator = new ProgressIndicator();
    rundownIndicator.setProgress(0);
    rundownIndicator.setMinWidth(70);
    rundownIndicator.setMinHeight(70);
    rundownBox.getChildren().addAll(rundownLabel, rundownIndicator);
    
    percentageBox.getChildren().addAll(utilizationBox, rundownBox);
    percentageBox.setPadding(new Insets(10, 20, 10, 20));
    percentageBox.setStyle("-fx-border-color: gray; =-fx-border-width: 1; -fx-border-style: dotted;");
    
    //add all to root
    getChildren().add(logoView);
    getChildren().add(buttonGroup);
    getChildren().add(timeBox);
    getChildren().add(percentageBox);
    
    
  }  
  
  //== Getter and setter  
  public void setTimeLabel(int time) {
    timeLabel.setText(time + " tu");
  }
  
  public Label getTimeLabel() {
    return timeLabel;
  }
  
  public int getTimeSleep() {
    return BASE_TIME - (int) timeSpeedSlider.getValue();
  }
  
  public void setUtilizationProgress(double value) {
    utilizationIndicator.setProgress(value);
  }
  
  public void setRundownProgress(double value) {
    rundownIndicator.setProgress(value);
  }
}
