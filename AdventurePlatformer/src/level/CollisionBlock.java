package level;

import javafx.scene.shape.Rectangle;

public class CollisionBlock {
    
    Rectangle blockCollision;
    
    CollisionBlock(int x, int y, int blockSize){
        blockCollision = new Rectangle(x, y, blockSize, blockSize);
        
    }
    
}
