package engine;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import level.LevelGenerator;

public class RenderEngine extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    
    ///////////////////////////////////////////////////
    
    public final static int WINDOW_WIDTH = 1280;
    public final static int WINDOW_HEIGHT = 720;
    public static final Group ROOT = new Group();
    public static final Group LV_GROUP = new Group();
    public static final Group UI_GROUP = new Group();
    public static Stage rootStage = new Stage();
    
    
    ///////////////////////////////////////////////////
    
    @Override public void start(Stage stage) {
        // Window Setup
        
        rootStage = stage;
        Scene rootScene = new Scene(ROOT, WINDOW_WIDTH, WINDOW_HEIGHT, Color.web("#D7BDE2"));
        stage.setScene(rootScene);
        stage.setTitle("RPG Game");
        stage.setResizable(true);
        stage.sizeToScene();
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();
        
        //Image image = new Image("/art/user_interface/crsr_plain.png");  //pass in the image path
        //rootScene.setCursor(new ImageCursor(image));
        
        ROOT.getChildren().add(LV_GROUP);
        ROOT.getChildren().add(UI_GROUP);
        
        LevelGenerator levelGen = new LevelGenerator();
        levelGen.generateRoom("TEST_LEVEL");
        
        Animation anim = new Animation();
    }

    public static void addToGroup(String group, Node... node){
        
        switch(group){
            case "LV":
                for (Node item : node) {
                    LV_GROUP.getChildren().add(item);
                }
                break;
            case "UI":
                for (Node item : node) {
                    UI_GROUP.getChildren().add(item);
                }
                break;
        }
        
    }
    
    public static void removeFromGroup(String group, Node... node){
        switch(group){
            case "LV":
                for (Node item : node) {
                    LV_GROUP.getChildren().remove(item);
                }
                break;
            case "UI":
                for (Node item : node) {
                    UI_GROUP.getChildren().remove(item);
                }
                break;
        }
    }
    
}
