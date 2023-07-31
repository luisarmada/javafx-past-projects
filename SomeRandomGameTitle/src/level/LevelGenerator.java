package level;

import engine.Main;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class LevelGenerator extends AnimationTimer{
    
    public static ArrayList<Shape> colShapeList = new ArrayList();
    public static ArrayList<Shape> platformShapeList = new ArrayList(); // Platforms entities can jump through but land on
    public static ArrayList<Rectangle> ladderColList = new ArrayList(); // A list of ladder collision boxes that the player can climb on
    public static ArrayList<PushableBox> pushBoxList = new ArrayList();
    
    public void createSlope(int x1, int y1, int x2, int y2){
        double smoothness = 250;
        for(int i = 0; i < smoothness; i++){
            Rectangle slopeRect = new Rectangle((x1 > x2 ? x2 : x1) + ((Math.abs(x1-x2) / smoothness) * i),
                    (y1 > y2 ? y2 : y1) + ((Math.abs(y1-y2) / smoothness) * i),
                    Math.abs(x1-x2)/smoothness,
                    Math.abs(y1-y2)/smoothness);
            Main.addToLevelGroup(slopeRect);
            colShapeList.add(slopeRect);
        }
    }
    
    public void createLadder(double startX, double startY, int height){
        int ladderCollisionWidth = 32;
        
        Rectangle ladderCollision = new Rectangle(startX + (ladderCollisionWidth/2), startY - height, ladderCollisionWidth, height);
        ladderCollision.setFill(Color.YELLOW);
        ladderColList.add(ladderCollision);
        Main.addToLevelGroup(ladderCollision);
        
    }

    @Override
    public void handle(long now) {
        
        //Main.ROOT.setScaleX(Main.ROOT.getScene().getWidth() / 1280);
        //Main.ROOT.setLayoutX(Math.round(((Main.ROOT.getScene().getWidth() / 1280) - 1) * (1280/2)));
        
        //Main.ROOT.setScaleY(Main.ROOT.getScene().getHeight() / 720);
        //Main.ROOT.setLayoutY(Math.round(((Main.ROOT.getScene().getHeight() / 1280) - 1) * (720/2)));
    }
    
}
