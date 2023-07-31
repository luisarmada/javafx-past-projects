package engine;

import level.*;
import java.util.Arrays;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainClass extends Application{

    public static void main(String[] args){
        launch(args);
    }
    
    ///////////////////////////////////////////////////////////
    
    public static Group root = new Group();
    final public static int WINDOW_WIDTH = 1280;
    final public static int WINDOW_HEIGHT = 720;
    
    Animation animationClass;
    public static LevelGenerator levelGen = new LevelGenerator();
    
    @Override public void start(Stage stage){
        
        // Window Setup
        Scene rootScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.web("#F1FAFB"));
        stage.setScene(rootScene);
        stage.setTitle("Adventure Platformer");
        stage.setResizable(false); stage.sizeToScene();
        stage.show();
        
        // Generate the specified room
        levelGen.generateRoom("Dungeon", 0, 0, true);
        
        // Start the game loop (AnimationTimer)
        animationClass = new Animation();
    }
    
    ///////////////////////////////////////////////////////////
    
    // Add to root function
    public static void addToRoot(Node... node){
        root.getChildren().addAll(Arrays.asList(node));
    }
    
    // Remove from root function
    public static void removeFromRoot(Node... node){
        root.getChildren().removeAll(Arrays.asList(node));
    }
}
