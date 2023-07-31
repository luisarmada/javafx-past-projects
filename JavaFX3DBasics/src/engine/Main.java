package engine;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

public class Main extends Application {

    final SmartGroup ROOT = new SmartGroup();
    final static double WINDOW_WIDTH = 1280;
    final static double WINDOW_HEIGHT = 720;
    
    private Stage rootStage;
    
    private double anchorX, anchorY;
    private double anchorAngleX, anchorAngleY;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    
    static Camera camera;
    
    @Override public void start(Stage stage) {
        rootStage = stage;
        camera = new PerspectiveCamera();
        
        Box box = new Box(100, 20, 50);
        Box box2 = new Box(50, 10, 30);
        
        box2.translateYProperty().set(-50);
        
        ROOT.getChildren().addAll(box, box2);
        
        ROOT.translateXProperty().set(WINDOW_WIDTH/2);
        ROOT.translateYProperty().set(WINDOW_HEIGHT/2);
        ROOT.translateZProperty().set(-1000);
        
        box.setMaterial(createMaterial("woodreal.jpg"));
        
        Scene rootScene = new Scene(ROOT, WINDOW_WIDTH, WINDOW_HEIGHT, true);
        rootScene.setFill(Color.SILVER);
        rootScene.setCamera(camera);
        rootStage.setScene(rootScene);
        rootStage.setTitle("JavaFX 3D Testing");
        rootStage.show();
        
        initMouseControl();
    }
    
    PhongMaterial createMaterial(String fileName){
       PhongMaterial material = new PhongMaterial();
       material.setDiffuseMap(new Image(getClass().getResourceAsStream("/engine/" + fileName)));
       return material;
    }
    
    private void initMouseControl(){
        Rotate xRotate;
        Rotate yRotate;
        ROOT.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);
        
        ROOT.getScene().setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });
        
        ROOT.getScene().setOnMouseDragged(event -> {
           angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
           angleY.set(anchorAngleY + (anchorX - event.getSceneX()));
        });
        
        rootStage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            ROOT.translateZProperty().set(ROOT.getTranslateZ() - delta);
        });
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    class SmartGroup extends Group{
        Rotate r;
        Transform t = new Rotate();
        
        void rotateByX(double ang){
            r = new Rotate(ang, Rotate.X_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
        
        void rotateByY(double ang){
            r = new Rotate(ang, Rotate.Y_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
    }
    
}
