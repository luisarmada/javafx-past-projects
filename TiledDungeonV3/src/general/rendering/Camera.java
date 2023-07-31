package general.rendering;

import general.entity.Character;
import general.Main;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

public class Camera extends AnimationTimer{
    
    public final Group grp;
    
    public double camZoom;
    
    double dragAnchorX, dragAnchorY;
    
    public double lvWidth = (Main.W_TILES) * Main.lGen.T_SIZE;
    public double lvHeight = (Main.H_TILES) * Main.lGen.T_SIZE;
    
    boolean isDraggingCam = false;
    
    public Character followCharacter = null;
    
    public Camera(double zoom, Group camGrp){
        camZoom = zoom;
        grp = camGrp;
        grp.setScaleX(camZoom);
        grp.setScaleY(camZoom);
        
        grp.getScene().setOnScroll(e -> {
            if (e.getDeltaY() < 0){
                zoomCam(-0.25/4);
            } else {
                zoomCam(0.25/4);
            }
        });
        
        grp.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            if(e.isSecondaryButtonDown()){
                dragAnchorX = e.getX() - grp.getLayoutX();
                dragAnchorY = e.getY() - grp.getLayoutY();
                isDraggingCam = true;
            }
        });
        
        grp.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
           if(e.isSecondaryButtonDown()){
                grp.setLayoutX(Math.round(e.getX() - dragAnchorX));
                grp.setLayoutY(Math.round(e.getY() - dragAnchorY));
           } 
        });
        
        grp.getScene().addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            if(!e.isSecondaryButtonDown()){
                isDraggingCam = false;
            }
        });
    }
    
    public void zoomCam(double zoomBy){
        if(camZoom + zoomBy > 0.25 && camZoom + zoomBy < 10){
            camZoom += zoomBy;
            grp.setScaleX(camZoom);
            grp.setScaleY(camZoom);
        }
    }

    @Override
    public void handle(long now) {
        if(followCharacter != null && !isDraggingCam){
            double xTo = (Main.STAGE.getWidth() / 2) - (followCharacter.midXPos * camZoom) - ((lvWidth - (lvWidth * camZoom)) / 2);
            double yTo = (Main.STAGE.getHeight() / 2) - (followCharacter.midYPos * camZoom) - ((lvHeight - (lvHeight * camZoom)) / 2);
            int interpolation = 25;
            double newX = grp.getLayoutX() + ((xTo - grp.getLayoutX()) / interpolation);
            double newY = grp.getLayoutY() + ((yTo - grp.getLayoutY()) / interpolation);
            double roundIfZoomLessThan = 1.0;
                //grp.setLayoutX(camZoom < roundIfZoomLessThan ? Math.round(newX) : newX);
                //grp.setLayoutY(camZoom < roundIfZoomLessThan ? Math.round(newY) : newY);
            grp.setLayoutX(Math.round(newX));
            grp.setLayoutY(Math.round(newY));
        }
    }
    
}
