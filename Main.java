import com.sun.javafx.css.converters.ShapeConverter;

import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Main extends Application
{
	private double scaleVal=1;
	private double zPos=-60;
	
    Shape3D selectedShape;
    Rotate boxRotateX = new Rotate(0, Rotate.X_AXIS);
    Rotate boxRotateY = new Rotate(0, Rotate.Y_AXIS);
    Rotate cylinderRotateX = new Rotate(0, Rotate.X_AXIS);
    Rotate cylinderRotateY = new Rotate(0, Rotate.Y_AXIS);
    Translate zTransform= new Translate(0,0, zPos);
    Button zoomOutButton= new Button("Zoom Out");
    Button zoomInButton= new Button("Zoom In");
    Button submitBackgroundColor= new Button("Submit Background Color");
    Button submitShapeColor= new Button("Submit Shape Color");
    Button addShape= new Button("Add Shape");

    Group shapesGroup = new Group();
    Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
    Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
    Scale scale = new Scale(1, 1, 1);
    SubScene subScene;
    PerspectiveCamera pCam = new PerspectiveCamera(true);
    Translate camZ = new Translate(0, 0, -60);
    Rotate camH = new Rotate(45, Rotate.X_AXIS);
    
    private Menu fileMenu; // Menus will be built in helper methods so make them fields
    private Menu textMenu;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        subScene = new SubScene(shapesGroup, 250, 250);
        subScene.setFill(Color.LAVENDER);

        pCam.getTransforms().addAll(camZ ,camH);

        subScene.setCamera(pCam);
        
        Label scaleLabel= new Label("Scale");
        Slider scaleSldr= new Slider(0.0, 100.0, 50.0);
        scaleSldr.setShowTickLabels(true);
        scaleSldr.setShowTickMarks(true);
        
        scaleSldr.valueProperty().addListener(((observable, oldValue, newValue) ->{
        	// double size= (scaleSldr.getValue()) / 50;
        	// scale.setY(size);
            // scale.setX(size);
            // scale.setZ(size);
        }));
        
        zoomInButton.setOnAction(event -> {
        	 if(zPos < -30) zPos ++;
        	 zTransform.setZ(zPos);
        });
        Label hLabel = new Label("Rotate Horizontally");
        Slider horizontalSlider = new Slider();
        horizontalSlider.setShowTickMarks(true);
        horizontalSlider.setMin(0);
        horizontalSlider.setMax(360);

        horizontalSlider.valueProperty().addListener((o, oldVal, newVal) ->
        {
            // if(selectedShape.equals(box))
            // {
            //     boxRotateX.setAngle((double)newVal);
            // }
            // else
            // {
            //     cylinderRotateX.setAngle((double)newVal);
            // }
        });

        Label vLabel = new Label("Rotate Vertically");
        Slider verticalSlider = new Slider();
        verticalSlider.setShowTickMarks(true);
        verticalSlider.setMin(0);
        verticalSlider.setMax(360);

        verticalSlider.valueProperty().addListener((o, oldVal, newVal) ->
        {
            // if(selectedShape.equals(box))
            // {
            //     boxRotateY.setAngle((double)newVal);
            // }
            // else
            // {
            //     cylinderRotateY.setAngle((double)newVal);
            // }
        });
        
        //DARKGREY,RED, DARKGREEN 
        ChoiceBox<String> shapesColorChoiceBox = new ChoiceBox<>();
        shapesColorChoiceBox.getItems().add("Grey");
        shapesColorChoiceBox.getItems().add("Green");
        shapesColorChoiceBox.getItems().add("Red"); 
        HBox shapesClr = new HBox(10, shapesColorChoiceBox, submitShapeColor);
      
        //DARKORANGE, GOLD, WHITE
        ChoiceBox<String> myBckGrndColorChoiceBox = new ChoiceBox<>();
        myBckGrndColorChoiceBox.getItems().add("White");
        myBckGrndColorChoiceBox.getItems().add("Yellow");
        myBckGrndColorChoiceBox.getItems().add("Orange"); 
        VBox sliderVBox = new VBox(15, hLabel, horizontalSlider, vLabel, verticalSlider, scaleLabel, scaleSldr, zoomOutButton, zoomInButton, shapesClr);
        HBox BackgroundClr = new HBox(10, myBckGrndColorChoiceBox, submitBackgroundColor);
        sliderVBox.setAlignment(Pos.CENTER);
        
        ChoiceBox<String> shapesChoiceBox = new ChoiceBox<>();
        shapesChoiceBox.getItems().add("Sphere");
        shapesChoiceBox.getItems().add("Box");
        shapesChoiceBox.getItems().add("Cylinder"); 
        HBox newShapes = new HBox(20, shapesChoiceBox, addShape);

        addShape.disableProperty().bind(
            shapesChoiceBox.valueProperty().isNull()
        );

        addShape.setOnAction(event -> {
        	Label ls= new Label(shapesChoiceBox.getSelectionModel().getSelectedItem());
        	createForm(shapesChoiceBox.getSelectionModel().getSelectedItem());
        });

        /*
        final double WIDTH = 300.0, HEIGHT = 200.0;
        outputLabel = new Label("Hello World");
        MenuBar menuBar = new MenuBar();
        buildFileMenu(primaryStage); // helper method to build the "file" menu
        menuBar.getMenus().addAll( fileMenu, textMenu); // add the instantiated Menus to menuBar
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setCenter( outputLabel);
        */
        
         // Create the 'root' of the menu system
        MenuBar menuBar = new MenuBar();
        // menuBar will have one Menu, titled "File"
        Menu fileMenu = new Menu("File");
        menuBar.getMenus().add(fileMenu);
        // fileMenu will have one MenuItem, titled "Exit"
        MenuItem saveItem = new MenuItem("Save");
        fileMenu.getItems().add(saveItem);
        // fileMenu will have one MenuItem, titled "Exit"
        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().add(exitItem);
        // Exit the program if the user selects exitItem
        exitItem.setOnAction(event -> {
         primaryStage.close();
        });
 
        GridPane gridPane = new GridPane();
        gridPane.add(menuBar , 0, 0);
        gridPane.add(subScene , 0, 1);
        gridPane.add(sliderVBox, 1, 1);
        gridPane.add(BackgroundClr, 0, 3);
        //gridPane.add(shapesClr, 1, 2);
        gridPane.add(newShapes, 1, 3);
        gridPane.setHgap(5);
        gridPane.setVgap(50);
        gridPane.setAlignment(Pos. CENTER);
        gridPane.setPadding( new Insets(100));
  	 
        Scene myScene = new Scene(gridPane);
        myScene.getStylesheets().add("style.css");
        primaryStage.setScene(myScene);
        primaryStage.show();
    }
  
    private void createForm(String shape)
    {
        Label label = new Label("What would you like in your shape?");
        GridPane gridPane = new GridPane();
        Label widthLabel = new Label("Width:");
        TextField widthText = new TextField("");
        widthText.setTooltip( new Tooltip("Please enter a valid width"));
        Label heightLabel = new Label("Height:");
        TextField heightText = new TextField("");
        heightText.setTooltip( new Tooltip("Please enter a valid height"));
        Label lengthLabel = new Label("Length:");
        TextField lengthText = new TextField("");
        lengthText.setTooltip( new Tooltip("Please enter a valid length"));

        Label xLabel = new Label("X Location:");
        TextField xText = new TextField("");
        lengthText.setTooltip( new Tooltip("Please enter a valid Y coordinate"));
        Label yLabel = new Label("Y Location:");
        TextField yText = new TextField("");
        lengthText.setTooltip( new Tooltip("Please enter a valid Y coordinate"));
        // continued from previous slide
        gridPane.add(widthLabel, 0, 0);
        gridPane.add(widthText, 1, 0);
        gridPane.add(lengthLabel, 0, 1);
        gridPane.add(lengthText, 1, 1);
        gridPane.add(heightLabel, 0, 2);
        gridPane.add(heightText, 1, 2);

        gridPane.add(xLabel, 0, 3);
        gridPane.add(xText, 1, 3);
        gridPane.add(yLabel, 0, 4);
        gridPane.add(yText, 1, 4);

        gridPane.setHgap( 10);
        gridPane.setVgap( 10);
        Button submit = new Button("Submit");
        Button cancel = new Button("Cancel");
        VBox vbox = new VBox(10, label, gridPane, submit, cancel);
        vbox.setAlignment(Pos. CENTER);
        vbox.setPadding( new Insets(25));
        BorderPane borderPane = new BorderPane(vbox);
        //Opening new stage:
        Scene alert = new Scene(borderPane);
        Stage secondary = new Stage();
        secondary.setScene(alert);
        cancel.setOnAction(event -> {
            secondary.close();
        });
        submit.setOnAction(event -> {
            switch (shape){
                case "Box": {
                    Box box = new Box(Double.valueOf(widthText.getText()),
                        Double.valueOf(heightText.getText()),
                        Double.valueOf(lengthText.getText())
                    );
                    box.getTransforms().add(new Translate(
                        Double.valueOf(xText.getText()),
                        Double.valueOf(yText.getText()),
                        0
                    ));
                    box.setOnMouseClicked(e -> {
                        if (selectedShape != null) {
                            selectedShape.getTransforms().removeAll(xRotate, yRotate);
                        }
                        selectedShape = box;
                        selectedShape.getTransforms().setAll(xRotate, yRotate, new Translate(0, 0, 0));
                    });
                    // box.getTransforms().add(new Translate(10,0,0));
                    box.getTransforms().addAll(zTransform, scale, boxRotateX, boxRotateY);
                    box.setMaterial(new PhongMaterial(Color.GREEN));

                    shapesGroup.getChildren().add(box);
                    break;
                }
            }
            secondary.close();
        });
        secondary.showAndWait();
    }

    private void buildFileMenu(Stage primaryStage)
    {
        fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(event -> {
            primaryStage .close();
        });
        fileMenu.getItems().add( exitItem);
    }
}
