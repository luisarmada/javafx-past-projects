package game;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    //////////////////////////////////////////////////////////////////////
    
    final static Group ROOT = new Group();
    final static int WINDOW_WIDTH = 1280;
    final static int WINDOW_HEIGHT = 720;
    
    @Override public void start(Stage stage) {
        
        // Window Setup
        Scene rootScene = new Scene(ROOT, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(rootScene);
        stage.sizeToScene();
        stage.setTitle("Pseudo Racing");
        stage.setResizable(false);
        stage.show();
        
        Road road = new Road();
        Animation anim = new Animation();
    }
    
    
    
    

}
