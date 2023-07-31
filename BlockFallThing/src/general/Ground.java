package general;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Ground {
    
    private Rectangle collision;
    int height;
    
    Ground(int winX, int winY, int height){
        this.height = height;
        collision = new Rectangle(0, winY - height, winX, height);
        collision.setFill(Color.web("#262730"));
    }
    
    Rectangle getCollision(){
        return collision;
    }
    
}
