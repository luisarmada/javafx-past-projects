package general.rendering;

import general.Main;
import general.rendering.LightSource;
import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile{
    
    public final Rectangle tileBox; // The box used to display the tile
    public final int gx, gy; // The column and row of each tile. Stands for grid x and grid y
    private final int tSize; // Width and Height of each tile
    public int tState = -1; // 0 = Path, 1 = Wall, -1 = Unvisited Tile (used during maze making process)
    public ArrayList<Tile> adjTileList = new ArrayList(); // Adjacent tiles used during maze making process
    public ArrayList<Tile> adjTileWithCornerList = new ArrayList(); // Adjacent tiles used during maze making process
    public Tile aboveTile, belowTile, leftTile, rightTile;
    public Tile topLeftTile, topRightTile, botLeftTile, botRightTile;
    public static final Color PATH_CLR = Color.web("#C4BFB0"); // "#C4BFB0" "#422835"
    public static final Color WALL_CLR = Color.web("#1c1117"); // "#262625" "#1C1117"
    public static final Color UNVIS_CLR = Color.web("#D7DBDD");
    public boolean isDoorTile = false;
    
    public final Rectangle visibilityBox; // The box used to overlay the tile, for visibility purposes
    
    public double tileLightLevel = 0;
    public ArrayList<LightSource> litByLightSourceList = new ArrayList();
    
    Tile(int row, int col, int size){
        gx = col;
        gy = row;
        tSize = size;
        tileBox = new Rectangle(gx * tSize, gy * tSize, tSize, tSize);
        visibilityBox = new Rectangle(gx * tSize, gy * tSize, tSize, tSize);
        tileBox.setFill(UNVIS_CLR);
        visibilityBox.setFill(WALL_CLR);
    }
    
    public void setState(int newState){
        tState = newState; // 0 = Path, 1 = Wall, -1 = unvisited
        Color tColor = null;
        switch(tState){
            case -1: tColor = UNVIS_CLR; break;
            case 0: tColor = PATH_CLR; break;
            case 1: tColor = WALL_CLR; break;
        }
        tileBox.setFill(tColor); // Set tile colour based on state
        
    }
    
    public boolean checkVisited(){
        // If tiles have 2 path tiles next to them in any position, mark them as visited.
        // Visited tiles become walls instantly and aren't checked during the backtracking process.
        int counter = 0;
        boolean visited = false;
        for (int i = 0; i < adjTileList.size(); i++) {
            if(adjTileList.get(i).tState == 0 && !adjTileList.get(i).isDoorTile){
                counter++;
                if(counter >= 2){
                    visited = true;
                    setState(1);
                    Main.lGen.addToWallList(this);
                    break;
                }
            }
        }
        return visited;
    }
    
    public void drawSprite(Group grp, Image tileImage){
        ImageView tileSprite = new ImageView(tileImage);
        
        tileSprite.setFitWidth(32);
        tileSprite.setFitHeight(32);
        tileSprite.setX(gx * tSize);
        tileSprite.setY(gy * tSize);
        
        grp.getChildren().add(tileSprite);
    }
    
    public void addToLightLevel(double level){
        tileLightLevel += level;
        visibilityBox.setOpacity(1 - tileLightLevel);
    }
    
}
