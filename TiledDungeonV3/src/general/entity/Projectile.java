package general.entity;

import javafx.scene.Group;

public class Projectile extends Character{

    Projectile(Group grp, double x, double y, int size, int speed, double angle){
        super(grp, x, y, size, size, speed);
        destroyOnCollision = true;
        moveAngle = angle;
        isMoving = true;
    }
    
}
