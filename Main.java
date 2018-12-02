import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Shape3D;
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
        SubScene subScene = new SubScene(shapesGroup, 200, 200);
        subScene.setFill(Color.DARKGREY);
        
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
        sliderVBox.setAlignment(Pos.CENTER);
        VBox vBox = new VBox(10, subScene, sliderVBox);
        vBox.setPadding(new Insets(25));

        Scene myScene = new Scene(vBox);
        primaryStage.setScene(myScene);
        primaryStage.show();
    }
}
