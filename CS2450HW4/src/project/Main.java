package project;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
import javafx.scene.*;
import javafx.event.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/*
 * Johnathan Fong
 * Fernando Ledesma
 * David Hau
 * Michael Wono
 * 
 * CS2450.02(F18)
 * Homework #4
 */

public class Main extends Application
{
	private double zPos=-60;
	
    Shape3D selectedShape;
    Button zoomOutButton= new Button("Zoom Out");
    Button zoomInButton= new Button("Zoom In");
    Button submitBackgroundColor= new Button("Submit Background Color");
    Button submitShapeColor= new Button("Submit Shape Color");
    Button addShape= new Button("Add Shape");

    Slider horizontalSlider;
    Slider verticalSlider;
    Slider zSlider;
    Slider xTranslateSlider;
    Slider yTranslateSlider;
    Slider zTranslateSlider;
    Slider scaleSldr;

    Group shapesGroup = new Group();
    // Rotate rotation = new Rotate();
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
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        subScene = new SubScene(shapesGroup, 560, 450);
        subScene.setFill(Color.LAVENDER);

        pCam.getTransforms().addAll(camH ,camZ);
        subScene.setCamera(pCam);
        
        Label scaleLabel= new Label("Scale");

        scaleSldr= new Slider(0.0, 2, 1);
        scaleSldr.setShowTickLabels(true);
        scaleSldr.setShowTickMarks(true);
        
        scaleSldr.valueProperty().addListener(((observable, oldValue, newValue) ->{

        	double size= scaleSldr.getValue();
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

        horizontalSlider = new Slider();
        horizontalSlider.setShowTickMarks(true);
        horizontalSlider.setMin(0);
        horizontalSlider.setMax(360);
        horizontalSlider.valueProperty().addListener((o, oldVal, newVal) ->
        {
            xRotate.setAngle((double)newVal);
        });

        Label vLabel = new Label("Rotate Along Y-Axis");

        verticalSlider = new Slider();
        verticalSlider.setShowTickMarks(true);
        verticalSlider.setMin(0);
        verticalSlider.setMax(360);
        verticalSlider.valueProperty().addListener((o, oldVal, newVal) ->
        {
            yRotate.setAngle((double)newVal);
        });
        
        Label zLabel = new Label("Rotate Along Z-Axis");

        zSlider = new Slider();
        zSlider.setShowTickMarks(true);
        zSlider.setMin(0);
        zSlider.setMax(360);
        zSlider.valueProperty().addListener((o, oldVal, newVal) ->
        {
            zRotate.setAngle((double)newVal);
        });

        Label xTranslateLabel = new Label("Translate Along X-Axis");

        xTranslateSlider = new Slider();
        xTranslateSlider.setShowTickMarks(true);
        xTranslateSlider.setMin(-100);
        xTranslateSlider.setMax(100);
        xTranslateSlider.valueProperty().addListener((o, oldVal, newVal) ->
        {
            translate.setX((double) newVal);
        });

        Label yTranslateLabel = new Label("Translate Along Y-Axis");

        yTranslateSlider = new Slider();
        yTranslateSlider.setShowTickMarks(true);
        yTranslateSlider.setMin(-100);
        yTranslateSlider.setMax(100);
        yTranslateSlider.valueProperty().addListener((o, oldVal, newVal) ->
        {
            translate.setY((double) newVal);
        });

        Label zTranslateLabel = new Label("Translate Along Z-Axis");

        zTranslateSlider = new Slider();
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
        	new Label(shapesChoiceBox.getSelectionModel().getSelectedItem());
        	createForm(shapesChoiceBox.getSelectionModel().getSelectedItem());
        });
        
        // Create the 'root' of the menu system
        MenuBar menuBar = new MenuBar();
        // menuBar will have one Menu, titled "File"
        Menu fileMenu = new Menu("File");
        menuBar.getMenus().add(fileMenu);
        // fileMenu will have one MenuItem, titled "New"
        MenuItem newItem = new MenuItem("New");
        fileMenu.getItems().add(newItem);
        // fileMenu will have one MenuItem, titled "Load"
        MenuItem loadItem = new MenuItem("Load");
        fileMenu.getItems().add(loadItem);
        // fileMenu will have one MenuItem, titled "Save"
        MenuItem saveItem = new MenuItem("Save");
        fileMenu.getItems().add(saveItem);
        // fileMenu will have one MenuItem, titled "Exit"
        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().add(exitItem);
        
        // New shapes scene
        newItem.setOnAction(event -> {
        	shapesGroup.getChildren().clear();
        });
        newItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCodeCombination.CONTROL_DOWN));
        
        // Load shapes into scene
        loadItem.setOnAction(event -> {
        	loadShapes();
        });
        loadItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCodeCombination.CONTROL_DOWN));
        
        // Save shapes in scene.
        saveItem.setOnAction(event ->{
        	saveShapes(shapesGroup.getChildren());
        });
        saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCodeCombination.CONTROL_DOWN));
        
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

        //DARKGREY,RED, DARKGREEN 
        ChoiceBox<String> shapesColorChoiceBox = new ChoiceBox<>();
        shapesColorChoiceBox.getItems().add("Grey");
        shapesColorChoiceBox.getItems().add("Green");
        shapesColorChoiceBox.getItems().add("Red"); 

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

        VBox vbox = new VBox(10, label, gridPane, shapesColorChoiceBox, submit, cancel);
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
                case "Sphere": {
                    Sphere  sphere= new Sphere(Double.valueOf(radiusText.getText()));
                    sphere.translateXProperty().set(Double.valueOf(xText.getText()));
                    sphere.translateYProperty().set(Double.valueOf(yText.getText()));
                    sphere.translateZProperty().set(Double.valueOf(zText.getText()));
                    sphere.setOnMouseClicked(e);
                    if (shapesColorChoiceBox.getValue().equals("Green")) {
                        sphere.setMaterial(new PhongMaterial(Color.GREEN));
                    } else if (shapesColorChoiceBox.getValue().equals("Red")) {
                        sphere.setMaterial(new PhongMaterial(Color.RED));
                    } else {
                        sphere.setMaterial(new PhongMaterial(Color.GREY));
                    }
 
                    shapesGroup.getChildren().add(sphere);
                    break;
                }
                case "Box": {
                    Box box = new Box(Double.valueOf(widthText.getText()),
                        Double.valueOf(heightText.getText()),
                        Double.valueOf(lengthText.getText())
                    );
                    box.translateXProperty().set(Double.valueOf(xText.getText()));
                    box.translateYProperty().set(Double.valueOf(yText.getText()));
                    box.translateZProperty().set(Double.valueOf(zText.getText()));
                    box.setOnMouseClicked(e);
                    if (shapesColorChoiceBox.getValue().equals("Green")) {
                        box.setMaterial(new PhongMaterial(Color.GREEN));
                    } else if (shapesColorChoiceBox.getValue().equals("Red")) {
                        box.setMaterial(new PhongMaterial(Color.RED));
                    } else {
                        box.setMaterial(new PhongMaterial(Color.GREY));
                    }

                    shapesGroup.getChildren().add(box);
                    break;
                }
                case "Cylinder": {
                    Cylinder cylinder = new Cylinder(
                        Double.valueOf(radiusText.getText()),
                        Double.valueOf(heightText.getText())
                    );
                    cylinder.translateXProperty().set(Double.valueOf(xText.getText()));
                    cylinder.translateYProperty().set(Double.valueOf(yText.getText()));
                    cylinder.translateZProperty().set(Double.valueOf(zText.getText()));
                    cylinder.setOnMouseClicked(e);
                    if (shapesColorChoiceBox.getValue().equals("Green")) {
                        cylinder.setMaterial(new PhongMaterial(Color.GREEN));
                    } else if (shapesColorChoiceBox.getValue().equals("Red")) {
                        cylinder.setMaterial(new PhongMaterial(Color.RED));
                    } else {
                        cylinder.setMaterial(new PhongMaterial(Color.GREY));
                    }

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
        fileMenu.getItems().add(exitItem);
    }
    
    public void saveShapes(ObservableList<Node> children) {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.getExtensionFilters().add(new ExtensionFilter("Text File", "*.txt"));
    	fileChooser.setTitle("Saving 3D Shapes");
    	File file = fileChooser.showSaveDialog(new Stage());
    	
    	try {
    		FileWriter writer = new FileWriter(file);
    		if(file != null) {
                writer.write("SHAPES_3D\r\n");
                writer.write("BG " + subScene.getFill());
                writer.write(System.getProperty("line.separator"));
    			for(int i = 0; i<children.size(); i++){
    					if(children.get(i).toString().charAt(0) == 'B') {
    						try {
                                Box b = (Box) children.get(i);
                                //Transform list: translate, xRotate, yRotate, zRotate, scale
                                Translate t = (Translate) b.getTransforms().get(0);
                                Rotate[] r = new Rotate[] {(Rotate) b.getTransforms().get(1), (Rotate) b.getTransforms().get(2), (Rotate) b.getTransforms().get(3)};
                                Scale s = (Scale) b.getTransforms().get(4);
    							String position = (t.getX() + b.getTranslateX()) + " " + (t.getY() + b.getTranslateY()) + " " + (t.getZ() + b.getTranslateZ());
    							String dimensions = (b.getHeight() + " " + b.getWidth() + " " + b.getDepth());
    							String color = colorDetector(((PhongMaterial) ((Shape3D) children.get(i)).getMaterial()).getDiffuseColor());
                                String scale = s.getX() + " "+ s.getY() + " "+ s.getZ();
    							String rotation = r[0].getAngle() + " " + r[1].getAngle() + " " + r[2].getAngle();
    									
    							writer.write("Box " + position + " " + dimensions + " " + color + " "+ scale + " "+ rotation );
    							writer.write(System.getProperty( "line.separator" ));
    							
    						} catch (IOException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
    					}
    					else if (children.get(i).toString().charAt(0) == 'S') {
    						try {
                                Sphere sphere = (Sphere) children.get(i);
                                //Transform list: translate, xRotate, yRotate, zRotate, scale
                                Translate t = (Translate) sphere.getTransforms().get(0);
                                Rotate[] r = new Rotate[] {(Rotate) sphere.getTransforms().get(1), (Rotate) sphere.getTransforms().get(2), (Rotate) sphere.getTransforms().get(3)};
                                Scale s = (Scale) sphere.getTransforms().get(4);
                                String position = (t.getX() + sphere.getTranslateX()) + " " + (t.getY() + sphere.getTranslateY()) + " " + (t.getZ() + sphere.getTranslateZ());
    							double dimensions = sphere.getRadius();
    							String color = colorDetector(((PhongMaterial) ((Shape3D) children.get(i)).getMaterial()).getDiffuseColor());
                                String scale = s.getX() + " "+ s.getY() + " "+ s.getZ();
    							String rotation = r[0].getAngle() + " " + r[1].getAngle() + " " + r[2].getAngle();
	
    							writer.write("Sphere " + position + " " + dimensions + " " + color + " "+ scale + " "+ rotation);
    							writer.write(System.getProperty( "line.separator" ));
    							
    						} catch (IOException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
    					}
    					else if (children.get(i).toString().charAt(0) == 'C') {
    						try {
                                Cylinder c = (Cylinder) children.get(i);
                                //Transform list: translate, xRotate, yRotate, zRotate, scale
                                Translate t = (Translate) c.getTransforms().get(0);
                                Rotate[] r = new Rotate[] {(Rotate) c.getTransforms().get(1), (Rotate) c.getTransforms().get(2), (Rotate) c.getTransforms().get(3)};
                                Scale s = (Scale) c.getTransforms().get(4);
    							String position = (t.getX() + c.getTranslateX()) + " " + (t.getY() + c.getTranslateY()) + " " + (t.getZ() + c.getTranslateZ());
    							String dimensions = (c.getHeight() + " " + c.getRadius());
    							String color = colorDetector(((PhongMaterial) ((Shape3D) children.get(i)).getMaterial()).getDiffuseColor());
                                String scale = s.getX() + " "+ s.getY() + " "+ s.getZ();
    							String rotation = r[0].getAngle() + " " + r[1].getAngle() + " " + r[2].getAngle();

    							writer.write("Cylinder " + position + " " + dimensions + " " + color + " "+ scale + " "+ rotation);
    							writer.write(System.getProperty( "line.separator" ));
                  
    						} catch (IOException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
    					}
    				}
    			}

    		writer.close();
    		
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}

    	System.out.print(children.toString());

    }
    
    //
    public void loadShapes()
    {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.getExtensionFilters().add(new ExtensionFilter("Text File", "*.txt"));
    	fileChooser.setTitle("Loading 3D Shapes");
    	File file = fileChooser.showOpenDialog(new Stage());

    	try {
    		FileReader reader = new FileReader(file);
    		
    		StringBuilder sb = new StringBuilder();
    		BufferedReader bufferedReader = new BufferedReader(reader);
    		String s;
    		
    		while((s = bufferedReader.readLine()) != null)
            {
                sb.append(s).append("\n");
            }
    		
    		bufferedReader.close();
    		
    		ArrayList<Shape3D> shapesList = new ArrayList<>();
    		
    		if(!sb.toString().equals("")) {
    			Scanner scanner = new Scanner(sb.toString());
    			String line = null;
    			boolean validFile = false;
    			

    			while(scanner.hasNextLine()) {
    				line = scanner.nextLine();
    				if(line.contains("SHAPES_3D"))
    				{
    					// Can parse file for info
    					validFile = true;
    				}
    				else if(!validFile)
    				{
    					// Do not continue parsing if it is an invalid file
    					break;
    				}
    				
    				// Clear the shapes from the screen
                    shapesGroup.getChildren().clear();
                    selectedShape = null;
    				
                    String[] temp = line.split(" ");
    				
    				Translate translate;
    				Rotate rotatex, rotatey, rotatez;
    				Scale scale;
    				if(temp[0].equals("Sphere"))
    				{
    					// 11 total Elements in temp
    					String[] positions = Arrays.copyOfRange(temp, 1, 4);
    					double dimensions = Double.parseDouble(temp[4]);
    					String material = temp[5];
    					String[] scales = Arrays.copyOfRange(temp, 6, 9);
    					String[] rotation = Arrays.copyOfRange(temp, 9, 12);//Double.parseDouble(temp[9]);
    					
    					Sphere sphere = new Sphere(dimensions);
                        // sphere
                        
                        if (material.equals("green")){
                            sphere.setMaterial(new PhongMaterial(Color.GREEN));
                        } else if (material.equals("red")) {
                            sphere.setMaterial(new PhongMaterial(Color.RED));
                        } else {
                            sphere.setMaterial(new PhongMaterial(Color.GREY));
                        }
                        
                        translate = new Translate(Double.valueOf(positions[0]), Double.valueOf(positions[1]), Double.valueOf(positions[2]));
                        scale = new Scale();
    					scale.setX(Double.valueOf(scales[0]));
    					scale.setY(Double.valueOf(scales[1]));
    					scale.setZ(Double.valueOf(scales[2]));
                        rotatex = new Rotate(Double.parseDouble(rotation[0]), Rotate.X_AXIS);
                        rotatey = new Rotate(Double.parseDouble(rotation[1]), Rotate.Y_AXIS);
                        rotatez = new Rotate(Double.parseDouble(rotation[2]), Rotate.Z_AXIS);
    					// sphere.setRotate(rotation);
    					sphere.getTransforms().addAll(translate, rotatex, rotatey, 
    							rotatez, scale);
    					sphere.setOnMouseClicked(e);
    					shapesList.add(sphere);
    				}
    				else if(temp[0].equals("Box"))
    				{
    					// 13 total Elements in temp
    					String[] positions = Arrays.copyOfRange(temp, 1, 4);
    					String[] dimensions = Arrays.copyOfRange(temp, 4, 7);
    					String material = temp[7];
                        String[] scales = Arrays.copyOfRange(temp, 8, 11);
                        String[] rotation = Arrays.copyOfRange(temp, 11, 14);
    					// double rotation = Double.parseDouble(temp[11]);
    					
    					Box box = new Box(Double.valueOf(dimensions[0]),
    	                        Double.valueOf(dimensions[1]),
    	                        Double.valueOf(dimensions[2]));
    					
    					if (material.equals("green")) {
    						box.setMaterial(new PhongMaterial(Color.GREEN));
    					} else if (material.equals("red")) {
    						box.setMaterial(new PhongMaterial(Color.RED));
    					} else {
    						box.setMaterial(new PhongMaterial(Color.GREY));
    					}
    					
    					translate = new Translate(Double.valueOf(positions[0]), Double.valueOf(positions[1]), Double.valueOf(positions[2]));
                        scale = new Scale();
    					scale.setX(Double.valueOf(scales[0]));
    					scale.setY(Double.valueOf(scales[1]));
    					scale.setZ(Double.valueOf(scales[2]));
                        rotatex = new Rotate(Double.parseDouble(rotation[0]), Rotate.X_AXIS);
                        rotatey = new Rotate(Double.parseDouble(rotation[1]), Rotate.Y_AXIS);
                        rotatez = new Rotate(Double.parseDouble(rotation[2]), Rotate.Z_AXIS);
    					// box.setRotate(rotation);
    					box.getTransforms().addAll(translate, rotatex, rotatey, 
    							rotatez, scale);
    					box.setOnMouseClicked(e);
    					shapesList.add(box);
    					
    				}
    				else if(temp[0].equals("Cylinder"))
    				{
    					// 12 total Elements in temp
    					String[] positions = Arrays.copyOfRange(temp, 1, 4);
    					String[] dimensions = Arrays.copyOfRange(temp, 4, 6);
    					String material = temp[6];
                        String[] scales = Arrays.copyOfRange(temp, 7, 10);
                        String[] rotation = Arrays.copyOfRange(temp, 10, 13);
    					// double rotation = Double.parseDouble(temp[10]);
    					
    					Cylinder cylinder = new Cylinder(
    	                        Double.valueOf(dimensions[1]),
    	                        Double.valueOf(dimensions[0]));
    					
    					if (material.equals("green")) {
    						cylinder.setMaterial(new PhongMaterial(Color.GREEN));
    					} else if (material.equals("red")) {
    						cylinder.setMaterial(new PhongMaterial(Color.RED));
    					} else {
    						cylinder.setMaterial(new PhongMaterial(Color.GREY));
    					}
    					
    					translate = new Translate(Double.valueOf(positions[0]), Double.valueOf(positions[1]), Double.valueOf(positions[2]));
                        scale = new Scale();
    					scale.setX(Double.valueOf(scales[0]));
    					scale.setY(Double.valueOf(scales[1]));
    					scale.setZ(Double.valueOf(scales[2]));
                        rotatex = new Rotate(Double.parseDouble(rotation[0]), Rotate.X_AXIS);
                        rotatey = new Rotate(Double.parseDouble(rotation[1]), Rotate.Y_AXIS);
                        rotatez = new Rotate(Double.parseDouble(rotation[2]), Rotate.Z_AXIS);
    					// cylinder.setRotate(rotation);
    					cylinder.getTransforms().addAll(translate, rotatex, rotatey, 
    							rotatez, scale);
    					cylinder.setOnMouseClicked(e);
    					shapesList.add(cylinder);
                    }
                    else if (temp[0].equals("BG")) 
                    {
                        subScene.setFill(Color.valueOf(temp[1]));
                    }
    				
    				if(line.isEmpty())
    				{
    					break; // Nothing else to parse
    				}
    			}
    			
    			shapesGroup.getChildren().addAll(shapesList);
    			
    			scanner.close();
    			
    		}

    		reader.close();
    		
    	} catch (IOException exception) {
    		// TODO Auto-generated catch block
    		exception.printStackTrace();
    	}

    }
    //
    public String colorDetector(Color color) {
    	
    	if(color.equals(Color.RED)) {
    		return "red";
    	}
    	else if(color.equals(Color.GREEN)) {
    		return "green";
    	}
    	else if(color.equals(Color.GRAY)) {
    		return "gray";
    	}
    	else {
    		return "pink";
    	}

    }

    EventHandler<Event> e = new EventHandler<Event>(){
        @Override
        public void handle(Event event) {
            if (selectedShape != null) {
                selectedShape.getTransforms().clear();
                selectedShape.getTransforms().addAll(
                    translate.clone(),
                    xRotate.clone(),
                    yRotate.clone(),
                    zRotate.clone(),
                    scale.clone()
                );
            }
            selectedShape = (Shape3D) event.getSource();
            
            if (selectedShape.getTransforms().size() != 0) {
                translate = (Translate) selectedShape.getTransforms().get(0);
                xRotate = (Rotate) selectedShape.getTransforms().get(1);
                yRotate = (Rotate) selectedShape.getTransforms().get(2);
                zRotate = (Rotate) selectedShape.getTransforms().get(3);
                scale = (Scale) selectedShape.getTransforms().get(4);
                selectedShape.getTransforms().clear();
            } else {
                xRotate = new Rotate(0, Rotate.X_AXIS);
                yRotate = new Rotate(0, Rotate.Y_AXIS);
                zRotate = new Rotate(0, Rotate.Z_AXIS);
                scale = new Scale(1, 1, 1);
                translate = new Translate();
            }
            selectedShape.getTransforms().addAll(translate, xRotate, yRotate, zRotate, scale);

            horizontalSlider.valueProperty().set(xRotate.getAngle());
            verticalSlider.valueProperty().set(yRotate.getAngle());
            zSlider.valueProperty().set(zRotate.getAngle());
            xTranslateSlider.valueProperty().set(translate.getX());
            yTranslateSlider.valueProperty().set(translate.getY());
            zTranslateSlider.valueProperty().set(translate.getZ());
            scaleSldr.valueProperty().set(scale.getX());
        }
    };
}