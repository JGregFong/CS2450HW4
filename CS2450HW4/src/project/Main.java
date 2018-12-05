import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
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
import javafx.scene.*;
import javafx.event.*;
import javafx.stage.Stage;

public class Main extends Application
{
	private double scaleVal=1;
	private double zPos=-60;
	
    Shape3D selectedShape;
    Button zoomOutButton= new Button("Zoom Out");
    Button zoomInButton= new Button("Zoom In");
    Button submitBackgroundColor= new Button("Submit Background Color");
    Button submitShapeColor= new Button("Submit Shape Color");
    Button addShape= new Button("Add Shape");

    Group shapesGroup = new Group();
    Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
    Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
    Rotate zRotate = new Rotate(0, Rotate.Z_AXIS);
    Scale scale = new Scale(1, 1, 1);
    Translate translate = new Translate();
    SubScene subScene;
    PerspectiveCamera pCam = new PerspectiveCamera(true);
    Translate camZ = new Translate(0, 0, zPos);
    Rotate camH = new Rotate(0, Rotate.X_AXIS);
    
    private Menu fileMenu; // Menus will be built in helper methods so make them fields
    private Menu textMenu;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        subScene = new SubScene(shapesGroup, 510, 400);
        subScene.setFill(Color.LAVENDER);

        pCam.getTransforms().addAll(camH ,camZ);
        subScene.setCamera(pCam);
        
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
             if(zPos < -30) zPos += 10;
             camZ.setZ(zPos);
        });

        zoomOutButton.setOnAction(event -> {
            if(zPos > -90) zPos -= 10;
            camZ.setZ(zPos);
        });
        
        Label hLabel = new Label("Rotate Along X-Axis");
        Slider horizontalSlider = new Slider();
        horizontalSlider.setShowTickMarks(true);
        horizontalSlider.setMin(0);
        horizontalSlider.setMax(360);
        horizontalSlider.valueProperty().addListener((o, oldVal, newVal) ->
        {
            xRotate.setAngle((double)newVal);
        });

        Label vLabel = new Label("Rotate Along Y-Axis");
        Slider verticalSlider = new Slider();
        verticalSlider.setShowTickMarks(true);
        verticalSlider.setMin(0);
        verticalSlider.setMax(360);
        verticalSlider.valueProperty().addListener((o, oldVal, newVal) ->
        {
            yRotate.setAngle((double)newVal);
        });
        
        Label zLabel = new Label("Rotate Along Z-Axis");
        Slider zSlider = new Slider();
        zSlider.setShowTickMarks(true);
        zSlider.setMin(0);
        zSlider.setMax(360);
        zSlider.valueProperty().addListener((o, oldVal, newVal) ->
        {
            zRotate.setAngle((double)newVal);
        });

        Label xTranslateLabel = new Label("Translate Along X-Axis");
        Slider xTranslateSlider = new Slider();
        xTranslateSlider.setShowTickMarks(true);
        xTranslateSlider.setMin(-100);
        xTranslateSlider.setMax(100);
        xTranslateSlider.valueProperty().addListener((o, oldVal, newVal) ->
        {
            translate.setX((double) newVal);
        });

        Label yTranslateLabel = new Label("Translate Along Y-Axis");
        Slider yTranslateSlider = new Slider();
        yTranslateSlider.setShowTickMarks(true);
        yTranslateSlider.setMin(-100);
        yTranslateSlider.setMax(100);
        yTranslateSlider.valueProperty().addListener((o, oldVal, newVal) ->
        {
            translate.setY((double) newVal);
        });

        Label zTranslateLabel = new Label("Translate Along Z-Axis");
        Slider zTranslateSlider = new Slider();
        zTranslateSlider.setShowTickMarks(true);
        zTranslateSlider.setMin(-100);
        zTranslateSlider.setMax(100);
        zTranslateSlider.valueProperty().addListener((o, oldVal, newVal) ->
        {
            translate.setZ((double) newVal);
        });

        //DARKGREY,RED, DARKGREEN 
        ChoiceBox<String> shapesColorChoiceBox = new ChoiceBox<>();
        shapesColorChoiceBox.getItems().add("Grey");
        shapesColorChoiceBox.getItems().add("Green");
        shapesColorChoiceBox.getItems().add("Red"); 
        HBox shapesClr = new HBox(10, shapesColorChoiceBox, submitShapeColor);

        submitShapeColor.setOnAction(event -> {
            if (selectedShape != null) {
                String v = shapesColorChoiceBox.getValue();
                if (v.equals("Grey")) {
                    selectedShape.setMaterial(new PhongMaterial(Color.GREY));
                } else if (v.equals("Green")) {
                    selectedShape.setMaterial(new PhongMaterial(Color.GREEN));
                } else {
                    selectedShape.setMaterial(new PhongMaterial(Color.RED));
                }
            }
        });
      
        //DARKORANGE, GOLD, WHITE
        ChoiceBox<String> myBckGrndColorChoiceBox = new ChoiceBox<>();
        myBckGrndColorChoiceBox.getItems().add("White");
        myBckGrndColorChoiceBox.getItems().add("Yellow");
        myBckGrndColorChoiceBox.getItems().add("Orange"); 
        VBox sliderVBox = new VBox(15,
            hLabel, horizontalSlider,
            vLabel, verticalSlider,
            zLabel, zSlider,
            xTranslateLabel, xTranslateSlider,
            yTranslateLabel, yTranslateSlider,
            zTranslateLabel, zTranslateSlider,
            scaleLabel, scaleSldr,
            zoomOutButton, zoomInButton,
            shapesClr
        );
        HBox BackgroundClr = new HBox(10, myBckGrndColorChoiceBox, submitBackgroundColor);
        submitBackgroundColor.setOnAction(event ->{ 
            String v = myBckGrndColorChoiceBox.getValue();
            if (v.equals("White")) {
                subScene.setFill(Color.WHITE);
            } else if (v.equals("Yellow")) {
                subScene.setFill(Color.YELLOW);
            } else {
                subScene.setFill(Color.ORANGE);
            }
        });

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
        
        Scene myScene = new Scene(borderPane);
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
        widthText.setDisable(shape.equals("Sphere") || shape.equals("Cylinder"));
        Label heightLabel = new Label("Height:");
        TextField heightText = new TextField("");
        heightText.setTooltip( new Tooltip("Please enter a valid height"));
        heightText.setDisable(shape.equals("Sphere"));
        Label lengthLabel = new Label("Length:");
        TextField lengthText = new TextField("");
        lengthText.setTooltip( new Tooltip("Please enter a valid length"));
        lengthText.setDisable(shape.equals("Sphere") || shape.equals("Cylinder"));
        Label radiusLabel = new Label("Radius:");
        TextField radiusText = new TextField("");
        radiusText.setTooltip( new Tooltip("Please enter a valid radius"));
        radiusText.setDisable(shape.equals("Box"));

        Label xLabel = new Label("X Location:");
        TextField xText = new TextField("");
        lengthText.setTooltip( new Tooltip("Please enter a valid Y coordinate"));
        Label yLabel = new Label("Y Location:");
        TextField yText = new TextField("");
        lengthText.setTooltip( new Tooltip("Please enter a valid Y coordinate"));
        Label zLabel = new Label("Z Location:");
        TextField zText = new TextField("");
        lengthText.setTooltip( new Tooltip("Please enter a valid Z coordinate"));
        

        gridPane.add(widthLabel, 0, 0);
        gridPane.add(widthText, 1, 0);
        gridPane.add(lengthLabel, 0, 1);
        gridPane.add(lengthText, 1, 1);
        gridPane.add(heightLabel, 0, 2);
        gridPane.add(heightText, 1, 2);
        gridPane.add(radiusLabel, 0, 3);
        gridPane.add(radiusText, 1, 3);

        gridPane.add(xLabel, 0, 4);
        gridPane.add(xText, 1, 4);
        gridPane.add(yLabel, 0, 5);
        gridPane.add(yText, 1, 5);
        gridPane.add(zLabel, 0, 6);
        gridPane.add(zText, 1, 6);

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

        EventHandler<Event> e = new EventHandler<Event>(){
            @Override
            public void handle(Event event) {
                if (selectedShape != null) {
                    selectedShape.getTransforms().removeAll(xRotate, yRotate, zRotate, scale, translate);
                    selectedShape.getTransforms().add(new Scale(scale.getX(), scale.getY(), scale.getZ()));
                    scale = new Scale();
                }
                selectedShape = (Shape3D) event.getSource();
                // translate = new Translate(
                //     selectedShape.getTranslateX(),
                //     selectedShape.getTranslateY(),
                //     selectedShape.getTranslateZ()
                // );
                selectedShape.getTransforms().addAll(xRotate, yRotate, zRotate, scale, translate);
            }
        };

        submit.setOnAction(event -> {
            switch (shape){
                case "Sphere": {
                    Sphere  sphere= new Sphere(Double.valueOf(radiusText.getText()));
                    sphere.getTransforms().add(new Translate(
                        Double.valueOf(xText.getText()),
                        Double.valueOf(yText.getText()),
                        Double.valueOf(zText.getText())
                    ));
                    sphere.setOnMouseClicked(e);
                    sphere.setMaterial(new PhongMaterial(Color.GREEN));

                    shapesGroup.getChildren().add(sphere);
                    break;
                }
                case "Box": {
                    Box box = new Box(Double.valueOf(widthText.getText()),
                        Double.valueOf(heightText.getText()),
                        Double.valueOf(lengthText.getText())
                    );
                    box.getTransforms().add(new Translate(
                        Double.valueOf(xText.getText()),
                        Double.valueOf(yText.getText()),
                        Double.valueOf(zText.getText())
                    ));
                    box.setOnMouseClicked(e);
                    box.setMaterial(new PhongMaterial(Color.GREEN));

                    shapesGroup.getChildren().add(box);
                    break;
                }
                case "Cylinder": {
                    Cylinder cylinder = new Cylinder(
                        Double.valueOf(radiusText.getText()),
                        Double.valueOf(heightText.getText())
                    );
                    cylinder.getTransforms().add(new Translate(
                        Double.valueOf(xText.getText()),
                        Double.valueOf(yText.getText()),
                        Double.valueOf(zText.getText())
                    ));
                    cylinder.setOnMouseClicked(e);
                    cylinder.setMaterial(new PhongMaterial(Color.GREEN));

                    shapesGroup.getChildren().add(cylinder);
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
