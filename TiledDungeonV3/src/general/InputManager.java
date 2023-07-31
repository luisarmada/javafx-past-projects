package general;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class InputManager {
    
    public ArrayList<KeyCode> heldKeys = new ArrayList();
    double angX, angY;
    
    public double lvMouseX, lvMouseY;
    
    Rectangle lvMouseRect = new Rectangle(0, 0, 5, 5);
    
    InputManager(Group grp){
        grp.getScene().addEventHandler(KeyEvent.KEY_PRESSED, e ->{
            if(!heldKeys.contains(e.getCode())){
                heldKeys.add(e.getCode());
            }
            if(null != e.getCode()) switch (e.getCode()) {
                case R:
                    heldKeys.clear();
                    Main.generateNewLevel();
                    break;
                case F11:
                    Main.toggleFS();
                    break;
                default:
                    break;
            } 
        });
        
        grp.getScene().addEventHandler(KeyEvent.KEY_RELEASED, e ->{
            if(heldKeys.contains(e.getCode())){
                heldKeys.remove(e.getCode());
            }
        });
        
        
        grp.getScene().addEventHandler(MouseEvent.ANY, e -> {
            updateMousePosition(grp, e);
        });
    }
    
    public void showMouseDebug(Group grp){
        lvMouseRect.setFill(Color.GREEN);
        grp.getChildren().add(lvMouseRect);
    }
    
    public void updateMousePosition(Group grp, MouseEvent e){
            double zoom = Main.cam.camZoom;
            double lvMidW = (grp.getLayoutX() + (Main.W_TILES * Main.lGen.T_SIZE)) / 2;
            double lvMidH = (grp.getLayoutY() + (Main.H_TILES * Main.lGen.T_SIZE)) / 2;
            lvMouseX =  lvMidW - ((lvMidW - (e.getX() - grp.getLayoutX())) / zoom);
            lvMouseY =  lvMidH - ((lvMidH - (e.getY() - grp.getLayoutY())) / zoom);
            
            //System.out.println(zoom);
            
            // Debug Mouse
            lvMouseRect.setX(Math.max(0, Math.min((Main.W_TILES-1) * Main.lGen.T_SIZE, lvMouseX)));
            lvMouseRect.setY(Math.max(0, Math.min((Main.H_TILES-1) * Main.lGen.T_SIZE, lvMouseY)));
            lvMouseRect.setWidth(15);
            lvMouseRect.setHeight(15);
            lvMouseRect.toFront();
    }
    
}