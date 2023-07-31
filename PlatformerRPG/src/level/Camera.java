package level;

import engine.RenderEngine;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

public class Camera {

    static Rectangle followRect;
    
    static double maxLevelX = 0.0;
    static double maxLevelY = 0.0;
    
    static double windowMouseX, windowMouseY, mouseX, mouseY;
    
    static Screen screen = Screen.getPrimary();
    static Rectangle2D bounds = screen.getVisualBounds();
    
    Camera(){
        RenderEngine.ROOT.getScene().addEventHandler(MouseEvent.MOUSE_MOVED, e-> {
            windowMouseX = e.getX();
            windowMouseY = e.getY();
        });
    }
    
    public static void setTarget(Rectangle newTarget){
       followRect = newTarget;
    }
    
    public static void update(){
        
        /////////////////////////////////////////// CAMERA VIEW /////////////////////////////////////////////////
        
        // Size of the in-game view
        double viewWidth = 1280; 
        double viewHeight = 720;
        
        // Camera Update X
        double targetMPX = followRect.getX() + (followRect.getWidth() / 2); // Follow target midpoint X
        double levelOffsetX = (targetMPX * (RenderEngine.ROOT.getScene().getWidth() / viewWidth)) - (RenderEngine.ROOT.getScene().getWidth() / 2);
        // Clamping
        if(levelOffsetX < 0){ // Min size
            levelOffsetX = 0;
        } else if(levelOffsetX > ((maxLevelX * (RenderEngine.ROOT.getScene().getWidth() / viewWidth)) - RenderEngine.ROOT.getScene().getWidth())){ // Max size
           levelOffsetX = (((maxLevelX * (RenderEngine.ROOT.getScene().getWidth() / viewWidth)) - RenderEngine.ROOT.getScene().getWidth())) ;
        }
        RenderEngine.ROOT.setScaleX(RenderEngine.ROOT.getScene().getWidth() / viewWidth);
        RenderEngine.ROOT.setLayoutX(((RenderEngine.ROOT.getScene().getWidth() / viewWidth) - 1) * (maxLevelX/2));
        RenderEngine.LV_GROUP.setLayoutX(Math.round(-levelOffsetX));
        
        // Camera Update Y
        double targetMPY = followRect.getY() + (followRect.getHeight() / 2); // Follow target midpoint Y
        double levelOffsetY = (targetMPY * (RenderEngine.ROOT.getScene().getHeight() / viewHeight)) - (RenderEngine.ROOT.getScene().getHeight() / 2);
        // Clamping
        if(levelOffsetY < 0){ // Min size
            levelOffsetY = 0;
        } else if(levelOffsetY > ((maxLevelY * (RenderEngine.ROOT.getScene().getHeight() / viewHeight)) - RenderEngine.ROOT.getScene().getHeight())){ // Max size
           levelOffsetY = (((maxLevelY * (RenderEngine.ROOT.getScene().getHeight() / viewHeight)) - RenderEngine.ROOT.getScene().getHeight())) ;
        }
        RenderEngine.ROOT.setScaleY(RenderEngine.ROOT.getScene().getHeight() / viewHeight);
        RenderEngine.ROOT.setLayoutY(((RenderEngine.ROOT.getScene().getHeight() / viewHeight) - 1) * (maxLevelY/2));
        RenderEngine.LV_GROUP.setLayoutY(Math.round(-levelOffsetY));
        
        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        // UPDATE MOUSE LOCATIONS WITH LEVEL OFFSET
        mouseX = windowMouseX + levelOffsetX;
        mouseY = windowMouseY + levelOffsetY;
        
    }
    
    public static void setFullscreen(boolean fullscreen){
        if(fullscreen){
            RenderEngine.rootStage.setX(bounds.getMinX());
            RenderEngine.rootStage.setY(bounds.getMinY());
            RenderEngine.rootStage.setWidth(bounds.getWidth());
            RenderEngine.rootStage.setHeight(bounds.getHeight());
            
            RenderEngine.rootStage.setFullScreen(true);
        } else {
            RenderEngine.rootStage.setFullScreen(false);
        }
    }
}
