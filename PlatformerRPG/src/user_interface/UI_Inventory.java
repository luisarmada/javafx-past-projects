package user_interface;

import engine.RenderEngine;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class UI_Inventory {

    // Inventory will be on the bottom left
    // 7 8 9 10 11 12
    // 13 14 15 16 17 18
    // 0 1 2 3 4 5 6
    
    static int cornerOffset /* from bottom left */ = 25;
    static int slotSpacing = 5;
    static int slotSize = 45;
    static int slotsPerRow = 7;
    static int numberOfRows /* excluding hotbar*/ = 3;
    
    static double slotVisibleOpacity = 1;
    
    static ArrayList<Rectangle> inventorySlots = new ArrayList();
    
    public static void create(){
        createRow(RenderEngine.WINDOW_HEIGHT - cornerOffset - slotSize, true); // Hotbar row
        
        for(int i = numberOfRows; i > 0; i -= 1){
            createRow((RenderEngine.WINDOW_HEIGHT - cornerOffset - slotSize) - (slotSpacing * i) - (slotSize * i), false);
        }
    }
    
    public static void toggleVisibility(){ // Of non hotbar slots
        
        for(int i = 0; i < inventorySlots.size(); i++){
            if(i >= slotsPerRow){ // if the slot is not in the hotbar, because hotbar slots are 0 - > slotsPerRow
                if(inventorySlots.get(i).getOpacity() > 0){
                    inventorySlots.get(i).setOpacity(0);
                } else {
                    inventorySlots.get(i).setOpacity(slotVisibleOpacity);
                }
            }
        }
        
    }
    
    static void createRow(double yCoord, boolean isHotbarRow){
        
        for(int i = 0; i < slotsPerRow ; i++){ 
           newSlot(cornerOffset+(i * (slotSize + slotSpacing)), yCoord, isHotbarRow);
        }
    }
    
    static void newSlot(double x, double y, boolean isHotbarSlot){
        Rectangle slotBorder = new Rectangle(x, y, slotSize, slotSize);
        slotBorder.setFill(Color.web("#c1c0c1"));
        slotBorder.setStroke(Color.web("#242224"));
        slotBorder.setStrokeWidth(2.5);

        if(isHotbarSlot){
            slotBorder.setOpacity(slotVisibleOpacity);
        } else {
            slotBorder.setOpacity(0);
        }
        
        inventorySlots.add(slotBorder);
        
        Text amountIndication = new Text(x, y, "99");
        amountIndication.setFont(Font.loadFont(UI_Inventory.class.getResourceAsStream("/art/fonts/manaspc.ttf"), 10));
        
        RenderEngine.addToGroup("UI", slotBorder); 
        RenderEngine.addToGroup("UI", amountIndication);
    }
}


