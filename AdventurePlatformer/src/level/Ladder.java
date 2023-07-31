package level;

import engine.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Ladder {

    public Rectangle ladderCollision;
    
    public int ladderThickness = 10;
    
    public String direction;
    
    public boolean canInteractWithLadder = true;
    
    Ladder(int x, int y, String dir){
        
        direction = dir;
        
        int rectX = 0;
        if(dir.equals("RIGHT")){
            rectX = x + (LevelGenerator.BLOCK_SIZE - ladderThickness);
        } else if (dir.equals("LEFT")){
            rectX = x;
        }
        
        ladderCollision = new Rectangle(rectX, y, ladderThickness, LevelGenerator.BLOCK_SIZE);
        ladderCollision.setFill(Color.LIGHTGREEN);
        MainClass.addToRoot(ladderCollision);
        
    }
    
    public void ladderLeave(){
        
    }
    
}
