package level;

import engine.*;
import entities.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.shape.Rectangle;

public class LevelGenerator {
    
    final static int BLOCK_SIZE = 40; // 32 blocks horizontally, 18 vertically
    
    public static ArrayList<Rectangle> colBlockArray = new ArrayList<>();
    public static ArrayList<Ladder> ladderArray = new ArrayList<>();
    public static ArrayList<DisplayTile> tileArray = new ArrayList<>();
    
    public static int currentRoomRow = 0;
    public static int currentRoomColumn = 0;
    
    public static void generateRoom(String levelName, int roomRow, int roomColumn, boolean spawn){
        currentRoomRow = roomRow;
        currentRoomColumn = roomColumn;
        
        // Remove existing Collision Blocks
        for(int i = 0; i < colBlockArray.size(); i++){
            MainClass.root.getChildren().remove(colBlockArray.get(i));
        }
        colBlockArray.removeAll(colBlockArray);
        
        for(int i = 0; i < ladderArray.size(); i++){
            MainClass.root.getChildren().remove(ladderArray.get(i).ladderCollision);
        }
        ladderArray.removeAll(ladderArray);
        
        // Read in specified room and draw it out
        ArrayList<String> roomLayers = new ArrayList();
        int roomXStart = roomColumn * 32 + roomColumn;
        int roomYStart = roomRow * 18 + roomRow;
        for(int i = roomYStart; i < roomYStart + 18; i++){
            String wholeLine = "";
            try {
                wholeLine = Files.readAllLines(Paths.get("src\\level\\levelLayouts\\" + levelName +".txt")).get(i);
            } catch (IOException ex) {
                Logger.getLogger(LevelGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
            roomLayers.add(wholeLine.substring(roomXStart, roomXStart + 32));
        }
                
        DisplayTile dTiles = new DisplayTile();
        
        // Check block at index
        for(int i = 0; i < roomLayers.size(); i++){
            for(int j = 0; j < roomLayers.get(i).length(); j++){
                switch(roomLayers.get(i).charAt(j)){
                    case '1':
                        createColBlock(j*BLOCK_SIZE, i*BLOCK_SIZE);
                        dTiles.tilex.add(j*BLOCK_SIZE); 
                        dTiles.tiley.add(i*BLOCK_SIZE);
                        break;
                    case 'X':
                        Player player;
                        if(spawn)
                            player = new Player((j*BLOCK_SIZE), ((i)*BLOCK_SIZE) - (Player.COLLISION_HEIGHT - BLOCK_SIZE + 1 /* +1 to allow space for collision testers*/));;
                        break;
                    case ']':
                        Ladder ladder1 = new Ladder((j)*BLOCK_SIZE, (i)*BLOCK_SIZE, "RIGHT");
                        ladderArray.add(ladder1);
                        break;
                    case '[':
                        Ladder ladder2 = new Ladder((j)*BLOCK_SIZE, (i)*BLOCK_SIZE, "LEFT");
                        ladderArray.add(ladder2);
                        break;
                }
            }
        
        }
        
        dTiles.initialiseTile();
        
    }
    
    static void createColBlock(int x, int y){
        CollisionBlock colBlock = new CollisionBlock(x, y, BLOCK_SIZE);
        colBlockArray.add(colBlock.blockCollision);
    }
}
