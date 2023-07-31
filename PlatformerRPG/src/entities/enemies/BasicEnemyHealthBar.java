package entities.enemies;

import engine.RenderEngine;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BasicEnemyHealthBar {
    
    public static ArrayList<Rectangle> borderRects = new ArrayList();
    
    public static void update(){
        for(int i = 0; i < EnemyManager.enemyColBlocks.size(); i++){
            
            Rectangle eCol = EnemyManager.enemyColBlocks.get(i);
            
            if(i > borderRects.size() - 1){
                Rectangle border  = new Rectangle(0, 0, 60, 10);
                border.setFill(Color.web("#212121"));
                border.setStroke(Color.web("#9E9E9E"));
                borderRects.add(border);
                RenderEngine.addToGroup("LV", border);
            }
            
            borderRects.get(i).setX(eCol.getX());
            borderRects.get(i).setY(eCol.getY());
        }
        
    }
    
}
