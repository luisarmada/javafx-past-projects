package engine;

import player.Player;
import java.util.Arrays;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import level.LevelGenerator;
import level.PushableBox;
import npcs.*;

public class Main extends Application {
        
    public static final int INIT_WINDOW_WIDTH = 1280;
    public static final int INIT_WINDOW_HEIGHT = 720;
    public static final Group ROOT = new Group();
    public static final Group LV_GRP = new Group();
    public static final Group UI_GRP = new Group();
    public static final Group PLYR_GRP = new Group();
    public static final Group SPELL_GRP = new Group();
    public static final Group ENEMY_GRP = new Group();
    
    public static Player player;
    public static UserInput input;
    
    @Override public void start(Stage stage) {
        Scene rootScene = new Scene(ROOT, INIT_WINDOW_WIDTH, INIT_WINDOW_HEIGHT, Color.web("#D7BDE2"));
        stage.setScene(rootScene);
        stage.setTitle("RPG Game");
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();
        
        ROOT.getChildren().addAll(LV_GRP, ENEMY_GRP, SPELL_GRP, PLYR_GRP, UI_GRP);
        
        //FillerNPC test = new FillerNPC();
        //test.start();
        
        Rectangle col = new Rectangle(0, 500, 500, 500);
        LevelGenerator.colShapeList.add(col);
        addToLevelGroup(col);
        
        Rectangle col3 = new Rectangle(500, 650, 800, 500);
        LevelGenerator.colShapeList.add(col3);
        addToLevelGroup(col3);
        
        Rectangle col4 = new Rectangle(0, 0, 200, 300);
        LevelGenerator.colShapeList.add(col4);
        addToLevelGroup(col4);
        
        Rectangle col5 = new Rectangle(0, 0, 30, 720);
        LevelGenerator.colShapeList.add(col5);
        addToLevelGroup(col5);
        
        Rectangle col6 = new Rectangle(1250, 0, 30, 720);
        LevelGenerator.colShapeList.add(col6);
        addToLevelGroup(col6);
        
        Rectangle plat = new Rectangle(300, 350, 700, 1);
        LevelGenerator.platformShapeList.add(plat);
        plat.setFill(Color.LIGHTGREEN);
        addToLevelGroup(plat);
        
        PushableBox push1 = new PushableBox(400, 100, 64, 64);
        push1.start();
        LevelGenerator.pushBoxList.add(push1);
        
        LevelGenerator levelGen = new LevelGenerator();
        levelGen.createLadder(850, 650, 300);
        //levelGen.createSlope(500, 500, 650, 650);
        levelGen.start();
    
        input = new UserInput();
        
        player = new Player();
        player.start();
        
        UserInterface ui = new UserInterface();
        ui.start();
        
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void addToLevelGroup(Node... children){
        LV_GRP.getChildren().addAll(Arrays.asList(children));
    }
            
}
