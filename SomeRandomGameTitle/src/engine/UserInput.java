package engine;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class UserInput extends AnimationTimer{
    
    public boolean disableInput = false;
    public ArrayList<KeyCode> heldInputKeys = new ArrayList(); 
    
    private CommandLine cmndLine = null;
    private final boolean enableCommandLine = true;
    public KeyCode commandLineKey = KeyCode.BACK_QUOTE;
    
    public KeyCode playerRightInput = KeyCode.D;
    public KeyCode playerLeftInput = KeyCode.A;
    public KeyCode playerUpInput = KeyCode.W;
    public KeyCode playerDownInput = KeyCode.S;
    public KeyCode playerJumpInput = KeyCode.SPACE;
    public KeyCode playerInteractInput = KeyCode.E;
    
    public double mouseX, mouseY;
    public boolean mousePrimaryHeld = false;
    
    UserInput(){
        
        Main.ROOT.getScene().addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if(!heldInputKeys.contains(e.getCode())){
                heldInputKeys.add(e.getCode());
            }
            
            if(e.getText().matches("[0-9]")){
                Main.player.mWisp.setAbility(Integer.parseInt(e.getText()));
            }
            
            // Command Line
            if(e.getCode() == commandLineKey && enableCommandLine){
                if(cmndLine == null){
                    cmndLine = new CommandLine();
                } else {
                    cmndLine.close();
                    cmndLine = null;
                }
            }
        });
        
        Main.ROOT.getScene().addEventHandler(KeyEvent.KEY_RELEASED, e->{
            if(heldInputKeys.contains(e.getCode())){
                heldInputKeys.remove(e.getCode());
            }
        });
        
        Main.ROOT.getScene().addEventHandler(MouseEvent.ANY, e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });
        
        Main.ROOT.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            if(e.getButton() == MouseButton.PRIMARY){
                mousePrimaryHeld = true;
            }
        });
        
        Main.ROOT.getScene().addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            if(e.getButton() == MouseButton.PRIMARY){
                mousePrimaryHeld = false;
            }
        });
        
    }

    @Override
    public void handle(long now) {
        
    }
    
    
}
