package level;

import engine.*;
import entities.*;
import entities.enemies.EnemyManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.shape.Rectangle;

public class LevelGenerator {
    
    /////////////////////////////////////////////////////////////////////////////////////
    
    public final static int BLOCK_SIZE = 40; // 32 blocks horizontally, 18 vertically (40 size)
    enum biomes{
        PLAINS, SNOW, JUNGLE, BEACH
    } 
    
    /////////////////////////////////////////////////////////////////////////////////////
    public static ArrayList<Rectangle> colBlockArray = new ArrayList<>();
    
    
    public Camera camera = new Camera();
    public SpriteManager sprManager = new SpriteManager();
    
    public int levelEndIndex = 0;
    
    public void generateRoom(String levelName){
        
        // Remove existing Collision Blocks
        for(int i = 0; i < colBlockArray.size(); i++){
            RenderEngine.removeFromGroup("LV", colBlockArray.get(i));
        }
        colBlockArray.removeAll(colBlockArray);
        
        
        // Read in specified room and draw it out
        ArrayList<String> roomLayers = new ArrayList();
        try{
            try (BufferedReader reader = new BufferedReader(new FileReader("src\\level\\layouts\\" + levelName +".txt"))) {
                String currentLine;
                int line = 0;
                int levelEndIndex = 0;
                while((currentLine = reader.readLine()) != null){
                    currentLine = currentLine.replaceAll("\\s+$", ""); // removes empty spaces at the end of each line
                    
                    if(currentLine.length() > levelEndIndex){  // set the max camera x to longest line in the file
                        levelEndIndex = currentLine.length();
                    }
                    
                    roomLayers.add(currentLine);
                    line++;
                }
                reader.close();
                double levelWidth = levelEndIndex * BLOCK_SIZE;
                double levelHeight = line * BLOCK_SIZE;
                Camera.maxLevelX = levelWidth; // Set camera max x size based on the longest line in the file
                Camera.maxLevelY = levelHeight; // Set camera max y size based on how many lines there are in world file
                
                // Set the knockback color overlay to size of the level
            }
        } catch (IOException e){}
        
        
        
        // Check block at index
        for(int i = 0; i < roomLayers.size(); i++){
            for(int j = 0; j < roomLayers.get(i).length(); j++){
                switch(roomLayers.get(i).charAt(j)){
                    case '1':
                        createColBlock(j*BLOCK_SIZE, i*BLOCK_SIZE);
                        break;
                    case 'P':
                        Player player = null;
                        player = new Player(j*BLOCK_SIZE, (i*BLOCK_SIZE) - (Player.COLLISION_HEIGHT - (BLOCK_SIZE - 1) /* +1 to allow space for collision testers*/));
                        Camera.setTarget(Player.collisionBox);
                        break;
                    case 'S':
                        EnemyManager.spawnEnemy(EnemyManager.enemyTypes.BLUE_SLIME, j*BLOCK_SIZE, i*BLOCK_SIZE);
                        break;
                    case 'M':
                        EnemyManager.spawnEnemy(EnemyManager.enemyTypes.MINOTAUR, j*BLOCK_SIZE, i*BLOCK_SIZE);
                        break;
                }
                
                
            }
        
        }
    }
    
    static void createColBlock(int x, int y){
        CollisionBlock colBlock = new CollisionBlock(x, y, BLOCK_SIZE + 1);
        colBlockArray.add(colBlock.blockCollision);
        RenderEngine.addToGroup("LV", colBlock.blockCollision);
        
    }
    
    
}
