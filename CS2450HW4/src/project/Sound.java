import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.util.Optional;


import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
public class Sound extends Application{
	
	//Jonathan Fong
	//Fernando Ledesma
	//David Hau
	//Michael Wono

	Button playButton = new Button("Play");
	Button pauseButton = new Button("Pause");
	Button stopButton = new Button("Stop");
	File musicFile;	
	public static void main(String args[]){
		launch(args);
	}
	
	public void start(Stage myStage) {
		HBox hbox = new HBox(10, playButton, pauseButton, stopButton);
		Scene myScene = new Scene(hbox);
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Audio Files (.mp3, .wav)", "*.mp3", "*.wav"));

		myScene.setOnKeyPressed(event ->{
			if(event.getCode() == KeyCode.O) {
				File music = fileChooser.showOpenDialog(myStage);
				
				if(music != null) {
					Media musicMedia = new Media(music.toURI().toString());
			
					MediaPlayer player = new MediaPlayer(musicMedia);
					
					playButton.setOnAction(e ->{
						player.stop();
						player.play();
						e.consume();
					});
					
					pauseButton.setOnAction(e ->{
						player.pause();
						e.consume();
					});
					
					stopButton.setOnAction(e ->{
						player.stop();
						e.consume();
					});		
				}
				
			}
		});

		myStage.setScene(myScene);
		
		myStage.show();
	}
}
