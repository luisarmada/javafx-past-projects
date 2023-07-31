package entities.enemies;

import engine.RenderEngine;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.shape.Rectangle;

public class AttackHitbox {

    static ArrayList<Rectangle> hitboxList = new ArrayList();
    
    public static void create(double x, double y, double width, double height, double damage){
        Rectangle attBox = new Rectangle(x, y, width, height);
        hitboxList.add(attBox);
        RenderEngine.addToGroup("LV", attBox);
    }
    
}
