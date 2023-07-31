package level.lighting;

import java.util.ArrayList;
import javafx.scene.shape.Rectangle;
import level.LevelGenerator;

public class lightBlock {
    
    int xLocation, yLocation;
    Rectangle displayBlock;
    ArrayList<Double> Opacities = new ArrayList();

    lightBlock(int newxLocation, int newyLocation){
        xLocation = newxLocation; yLocation = newyLocation;
        displayBlock = new Rectangle(xLocation, yLocation, LevelGenerator.BLOCK_SIZE, LevelGenerator.BLOCK_SIZE);
    }
    
}
