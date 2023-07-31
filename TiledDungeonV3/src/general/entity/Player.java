package general.entity;

import general.Main;
import general.rendering.LevelGenerator;
import general.rendering.Tile;
import general.rendering.LightSource;
import java.util.Random;
import javafx.scene.input.KeyCode;

public class Player extends Character{
    
    private final KeyCode[] moveInput = new KeyCode[]{KeyCode.W, KeyCode.A, KeyCode.S, KeyCode.D};
    private final KeyCode[] shootInput = new KeyCode[]{KeyCode.UP, KeyCode.LEFT, KeyCode.DOWN, KeyCode.RIGHT};
    
    Random rand = new Random();
    
    public boolean wantsShoot = false;
    public double shootAngle = 0;
        
    private final LightSource lightSource;
    
    long lastUpdate = 0;
    
    public Weapon sword;
    
    public Player(LevelGenerator lGen, SpriteBank sBank, Tile spawnTile, int w, int h){
        super(lGen.tGroup, (spawnTile.gx * Main.lGen.T_SIZE) + (Main.lGen.T_SIZE/2) - (w/2), (spawnTile.gy * Main.lGen.T_SIZE) + (Main.lGen.T_SIZE/2) - (h/2),w, h, 2);
        this.grp = lGen.tGroup;
        lightSource = new LightSource(lGen);
        sword = new Weapon(grp, sBank.sword1);
    }
    
    public void setMoveDirection(){
        boolean up = Main.input.heldKeys.contains(moveInput[0]);
        boolean down = Main.input.heldKeys.contains(moveInput[2]);
        boolean left = Main.input.heldKeys.contains(moveInput[1]);
        boolean right = Main.input.heldKeys.contains(moveInput[3]);
        if(up || down || left || right){ // Cancelling out input
            isMoving = true;
            if(up && !down && !left && !right){ // N
                moveAngle = 270;
            } else if(up && !down && !left && right){ // NE
                moveAngle = 315;
            } else if(!up && !down && !left && right){ // E
                moveAngle = 0;
            } else if(!up && down && !left && right){ // SE
                moveAngle = 45;
            } else if(!up && down && !left && !right){ // S
                moveAngle = 90;
            } else if(!up && down && left && !right){ // SW
                moveAngle = 135;
            } else if(!up && !down && left && !right){ // W
                moveAngle = 180;
            } else if(up && !down && left && !right){ // NW
                moveAngle = 225;
            } else {
                isMoving = false;
            }
        } else {
            isMoving = false;
        }
    }
    
    public void shootProjectileCheck(){
        boolean up = Main.input.heldKeys.contains(shootInput[0]);
        boolean down = Main.input.heldKeys.contains(shootInput[2]);
        boolean left = Main.input.heldKeys.contains(shootInput[1]);
        boolean right = Main.input.heldKeys.contains(shootInput[3]);
        if(up || down || left || right){ // Cancelling out input
            if(up && !down && !left && !right){ // N
                createProjectile(270);
            } else if(up && !down && !left && right){ // NE
                createProjectile(315);
            } else if(!up && !down && !left && right){ // E
                createProjectile(0);
            } else if(!up && down && !left && right){ // SE
                createProjectile(45);
            } else if(!up && down && !left && !right){ // S
                createProjectile(90);
            } else if(!up && down && left && !right){ // SW
                createProjectile(135);
            } else if(!up && !down && left && !right){ // W
                createProjectile(180);
            } else if(up && !down && left && !right){ // NW
                createProjectile(225);
            }
        }
        
    }
    
    public void createProjectile(double sAngle){
        int size = 5;
        int spray = 7;
        
        //int randomSpray = rand.nextInt(spray * 2) - spray;
        int randomSpray = 0;
        Projectile p = new Projectile(grp, xPos + (cbWidth/2) - (size/2),  yPos + (cbHeight/2) - (size/2), size, 5, sAngle + randomSpray);
        p.start();
    }
    
    @Override
    public void handle(long now) {
        super.handle(now);
        setMoveDirection();
        
        if(now - lastUpdate >= 80_000_000 && wantsShoot){
            lastUpdate = now;
            //shootProjectileCheck();
            createProjectile(shootAngle);
        }
        
        if(currentTile != lightSource.centerTile){
            lightSource.createLightRing(currentTile, 6.5, 7.6);
        }
        
        double swordAngle = (double) Math.toDegrees(Math.atan2(yPos - Main.input.lvMouseY, xPos - Main.input.lvMouseX)) - 90;
        
        sword.updatePosition(midXPos - 9, midYPos - 12, swordAngle);
        
    }
}
