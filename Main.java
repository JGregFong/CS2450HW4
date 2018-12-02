import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
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
    Button submitNewShape= new Button("Submit Shape");
    
    private Menu fileMenu; // Menus will be built in helper methods so make them fields
    private Menu textMenu;
    private Label outputLabel; 

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
    	
		Scale scale= new Scale(scaleVal, 1,1 );
        Box box = new Box(30, 50, 30);
        box.setLayoutY(50);
        box.setLayoutX(100);
        box.setMaterial(new PhongMaterial(Color.LIGHTBLUE));
        selectedShape = box;
        box.setOnMouseClicked(event -> {
            selectedShape = box;
        });
        box.getTransforms().add(new Translate(10,0,0));
        box.getTransforms().addAll(zTransform, scale, boxRotateX, boxRotateY);
        
        /*
        Sphere sphere = new Sphere(10);
        sphere.setLayoutY(50);
        sphere.setLayoutX(100);
        sphere.setMaterial(new PhongMaterial(Color.LIGHTBLUE));
        selectedShape = sphere;
        box.setOnMouseClicked(event -> {
            selectedShape = sphere;
        });
        sphere.getTransforms().add(new Translate(10,0,0));
        sphere.getTransforms().addAll(zTransform, scale, boxRotateX, boxRotateY);
        */
        
        Cylinder cylinder = new Cylinder(20, 50, 10);
        cylinder.setLayoutX(100);
        cylinder.setLayoutY(120);
        cylinder.setMaterial(new PhongMaterial(Color.LIGHTBLUE));
        cylinder.setOnMouseClicked(event -> {
            selectedShape = cylinder;
        });
        cylinder.getTransforms().add(new Translate(10,0,0));
        cylinder.getTransforms().addAll(zTransform,scale, cylinderRotateX, cylinderRotateY);

        Group shapesGroup = new Group(box, cylinder);
        SubScene subScene = new SubScene(shapesGroup, 250, 250);
        subScene.setFill(Color.LAVENDER);
        
        Label scaleLabel= new Label("Scale");
        Slider scaleSldr= new Slider(0.0, 100.0, 50.0);
        scaleSldr.setShowTickLabels(true);
        scaleSldr.setShowTickMarks(true);
        
        scaleSldr.valueProperty().addListener(((observable, oldValue, newValue) ->{
        	double size= (scaleSldr.getValue()) / 50;
        	scale.setY(size);
            scale.setX(size);
            scale.setZ(size);
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
            if(selectedShape.equals(box))
            {
                boxRotateX.setAngle((double)newVal);
            }
            else
            {
                cylinderRotateX.setAngle((double)newVal);
            }
        });

        Label vLabel = new Label("Rotate Vertically");
        Slider verticalSlider = new Slider();
        verticalSlider.setShowTickMarks(true);
        verticalSlider.setMin(0);
        verticalSlider.setMax(360);

        verticalSlider.valueProperty().addListener((o, oldVal, newVal) ->
        {
            if(selectedShape.equals(box))
            {
                boxRotateY.setAngle((double)newVal);
            }
            else
            {
                cylinderRotateY.setAngle((double)newVal);
            }
        });
        
        
        VBox sliderVBox = new VBox(10, hLabel, horizontalSlider, vLabel, verticalSlider, scaleLabel, scaleSldr, zoomOutButton, zoomInButton);
        HBox shapeBackgroundClr = new HBox(submitNewShape);
        sliderVBox.setAlignment(Pos.CENTER);
     
        
       
   
   
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
  	 gridPane.add( sliderVBox, 1, 1);
  	 gridPane.add(shapeBackgroundClr, 0, 2);
  	 gridPane.setAlignment(Pos. CENTER);
  	 gridPane.setPadding( new Insets(50));
  	 
        Scene myScene = new Scene(gridPane);
        primaryStage.setScene(myScene);
        primaryStage.show();
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
