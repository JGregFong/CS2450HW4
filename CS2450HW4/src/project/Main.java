import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
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
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Main extends Application
{
	private double scaleVal=1;
	private double zPos=-60;
	
    Shape3D selectedShape;
    Rotate boxRotateX = new Rotate(0, Rotate.X_AXIS);
    Rotate boxRotateY = new Rotate(0, Rotate.Y_AXIS);
    Rotate sphereRotateX = new Rotate(0, Rotate.X_AXIS);
    Rotate sphereRotateY = new Rotate(0, Rotate.Y_AXIS);
    Rotate cylinderRotateX = new Rotate(0, Rotate.X_AXIS);
    Rotate cylinderRotateY = new Rotate(0, Rotate.Y_AXIS);
    Translate Translating= new Translate(50,50, 0);
    Button submitBackgroundColor= new Button("Submit Background Color");
    Button submitShapeColor= new Button("Submit Shape Color");
    Button addShape= new Button("Add Shape");
    
    private Menu fileMenu; // Menus will be built in helper methods so make them fields


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
        //Translating= new Translate(300,100, 0);
        //Translating, 
        box.getTransforms().addAll(scale, boxRotateX, boxRotateY);
        
   
        Sphere sphere = new Sphere(10);
        sphere.setLayoutY(100);
        sphere.setLayoutX(200);
        sphere.setMaterial(new PhongMaterial(Color.LIGHTBLUE));
        selectedShape = sphere;
        sphere.setOnMouseClicked(event -> {
            selectedShape = sphere;
        });
        //Translating= new Translate(100,50, 0);
        //Translating,
		sphere.getTransforms().addAll(scale, sphereRotateX, sphereRotateY);
        
        
        Cylinder cylinder = new Cylinder(20, 50, 10);
        cylinder.setLayoutX(100);
        cylinder.setLayoutY(120);
        cylinder.setMaterial(new PhongMaterial(Color.LIGHTBLUE));
        cylinder.setOnMouseClicked(event -> {
            selectedShape = cylinder;
        });
        
        
        //Translating,
        //Translating= new Translate(200,70, 0);
        cylinder.getTransforms().addAll(scale, cylinderRotateX, cylinderRotateY);

        Group shapesGroup = new Group(box, cylinder, sphere);
        SubScene subScene = new SubScene(shapesGroup, 510, 400);
        subScene.setFill(Color.LAVENDER); 
        
        Label scaleLabel= new Label("Scale");
        Slider scaleSldr= new Slider(0.0, 100.0, 50.0);
        scaleSldr.setShowTickLabels(true);

        
        scaleSldr.valueProperty().addListener(((observable, oldValue, newValue) ->{
        	double size= (scaleSldr.getValue()) / 50;
        	scale.setY(size);
            scale.setX(size);
            scale.setZ(size);
        }));
        
   
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
            else if(selectedShape.equals(sphere))
            {
                sphereRotateX.setAngle((double)newVal);
            }
            else
            {
                cylinderRotateX.setAngle((double)newVal);
            }
        });

        Label trHLabel = new Label("Translate Horizontally");
        Slider transHorizontalSlider = new Slider();
        transHorizontalSlider.setShowTickMarks(true);
        transHorizontalSlider.setMin(0);
        transHorizontalSlider.setMax(400);
        
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
            else if(selectedShape.equals(sphere))
            {
                sphereRotateY.setAngle((double)newVal);
            }
            else
            {
                cylinderRotateY.setAngle((double)newVal);
            }
        });
        
        Label trVLabel = new Label("Translate Vertically");
        Slider transVerticalSlider = new Slider();
        transVerticalSlider.setShowTickMarks(true);
        transVerticalSlider.setMin(0);
        transVerticalSlider.setMax(400);
        
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
        VBox sliderVBox = new VBox(15, hLabel, horizontalSlider, vLabel, verticalSlider, scaleLabel, scaleSldr, shapesClr, trVLabel, transVerticalSlider, trHLabel, transHorizontalSlider);
        HBox BackgroundClr = new HBox(10, myBckGrndColorChoiceBox, submitBackgroundColor);
        sliderVBox.setAlignment(Pos.CENTER);
        
        ChoiceBox<String> shapesChoiceBox = new ChoiceBox<>();
        shapesChoiceBox.getItems().add("Sphere");
        shapesChoiceBox.getItems().add("Box");
        shapesChoiceBox.getItems().add("Cylinder"); 
        HBox newShapes = new HBox(20, shapesChoiceBox, addShape);
   
     
        addShape.setOnAction(event -> {
        	Label ls= new Label(shapesChoiceBox.getSelectionModel().getSelectedItem());
        	createForm();
        	});
   
    
        
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
    
       
        HBox hboxBottom= new HBox(20, BackgroundClr, shapesClr, newShapes);
        hboxBottom.setPadding(new Insets(20,0,20,0));
        buildFileMenu(primaryStage); // helper method to build the "file" menu
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setCenter(subScene);
        borderPane.setRight(sliderVBox);
        borderPane.setBottom(hboxBottom);
        borderPane.setPadding(new Insets(0, 20, 80, 20));
        
        /*
        PerspectiveCamera pCamera = new PerspectiveCamera( true);
        Rotate horizontalRotate = new Rotate(30, Rotate.Y_AXIS);
        Rotate verticalRotate = new Rotate(15, Rotate.X_AXIS);
        pCamera.getTransforms().addAll(horizontalRotate, verticalRotate);
        */
        
        Scene myScene = new Scene(borderPane);
        myScene.getStylesheets().add("style.css");
        primaryStage.setScene(myScene);
        primaryStage.show();
    
        
    }
  
    private void createForm()
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
    // continued from previous slide
    gridPane.add(widthLabel, 0, 0);
    gridPane.add(widthText, 1, 0);
    gridPane.add(lengthLabel, 0, 1);
    gridPane.add(lengthText, 1, 1);
    gridPane.add(heightLabel, 0, 2);
    gridPane.add(heightText, 1, 2);
    gridPane.setHgap( 10);
    gridPane.setVgap( 10);
    Button button = new Button("Submit");
    VBox vbox = new VBox(10, label, gridPane, button);
    vbox.setAlignment(Pos. CENTER);
    vbox.setPadding( new Insets(25));
    BorderPane borderPane = null;
	borderPane.setCenter(vbox);
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
