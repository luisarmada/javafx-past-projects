package general;

import general.entity.*;
import general.rendering.*;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    
    public static final int DIS_W = 1600;
    public static final int DIS_H = 900;
    public static final Group ROOT = new Group();
    
    public static LevelGenerator lGen;
    public static InputManager input;
    public static Camera cam;
    public static Pathfinder pFinder;
    public static Player player;
    public static LevelDrawer lvDrawer;
    public static SpriteBank spriteBank;
    
    public static Stage STAGE;
    
    private static double pfsX, pfsY, pfsW, pfsH;
    
    public static final int W_TILES = 63;
    public static final int H_TILES = 35;
    
    @Override public void start(Stage stage) {
        STAGE = stage;
        Scene rootScene = new Scene(ROOT, DIS_W, DIS_H, Tile.WALL_CLR);
        stage.setScene(rootScene);
        
        // Level Drawer
        lvDrawer = new LevelDrawer();
        
        // Draw Level
        lGen = new LevelGenerator(ROOT, W_TILES, H_TILES, lvDrawer);
        
        // Cam
        cam = new Camera(2, ROOT); // 320, 180 ; 16 : 9 ; x0.25
        cam.start();
        
        // Input Setup
        input = new InputManager(ROOT);
        
        // Pathfinder Setup
        pFinder = new Pathfinder();
        
        spriteBank = new SpriteBank();
        
        generateNewLevel();
        
        // Window Setup
        stage.setTitle("Tiled Dungeon");
        stage.sizeToScene();
        stage.setFullScreenExitHint("");
        stage.show();
        
    }

    public static void generateNewLevel(){
        lvDrawer.clear();
        boolean drawDungeon = false;
        boolean drawPathfinder = false;
        boolean drawVisibility = true;
        
        lGen.generateDungeon(ROOT, drawDungeon, drawVisibility);
        if(drawPathfinder){
            pFinder.pathfind(lGen.getTileAt(1, 1), lGen.getTileAt(W_TILES - 2, H_TILES - 2), lvDrawer, drawPathfinder).forEach((pTile) -> {
                lvDrawer.addDrawTiles(-1, -1, Color.web("#2E86C1"), new ArrayList<>(Arrays.asList(pTile)));
            });
        }
        
        lvDrawer.drawTileSprites(ROOT, lGen.pathList, lGen.wallList);
        
        lvDrawer.start();
        
        if(player != null){ player.stop(); }
        player = new Player(lGen, spriteBank, lGen.getTileAt(1, 1), 15, 10);
        player.setupSprites(spriteBank.playerIdleAnim, 150_000_000, spriteBank.playerWalkAnim, 100_000_000, 1, -8, -22, -9);
        player.sword.ivSprite.toFront();
        //cam.followCharacter = player;
        player.start();
        
        lGen.sendVisTilesToFront();
        
        input.showMouseDebug(ROOT);
    }
    
    public static void toggleFS(){
        if(!STAGE.isFullScreen()){
            pfsX = STAGE.getX();
            pfsY = STAGE.getY();
            pfsW = STAGE.getWidth();
            pfsH = STAGE.getHeight();
            STAGE.setFullScreen(true);
            STAGE.setMaximized(true);
        } else {
            STAGE.setFullScreen(false);
            STAGE.setMaximized(false);
            STAGE.setX(pfsX);
            STAGE.setY(pfsY);
            STAGE.setWidth(pfsW);
            STAGE.setHeight(pfsH);
        }
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}
