package game;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class Road {

    final static int HORIZON_OFFSET = 375;
    static int horizonYPoint = Main.WINDOW_HEIGHT - HORIZON_OFFSET; // Window Height - horizonOffsetY = horizonPoint
    static int roadStartX = (int) Main.WINDOW_WIDTH/2;
    static int roadXAtHorizon;
    int windowMouseX;
    
    static int roadSegments = 4;
    static ArrayList<Line> roadConstructorLines = new ArrayList();
    static ArrayList<Polygon> roadStructurePolys = new ArrayList();
    static ArrayList<Polygon> roadKerbPolysLeft = new ArrayList();
    static ArrayList<Polygon> roadKerbPolysRight = new ArrayList();
    static Line horizonLine;
    
    static ArrayList<Line> segmentDividers = new ArrayList();
    static ArrayList<Double> segmentDividersSpeed = new ArrayList();
    
    static Polygon grassPolyLeft;
    
    public static long lastUpdate;
    public static int updateNumber = 0;
    
    final static Group GRP_CONSTRUCTOR = new Group();
    final static Group GRP_DISPLAY = new Group();
    
    Road(){
        createRoadConstructors();
        changeRoadHorizonPoint(Main.WINDOW_WIDTH/2);
        Main.ROOT.getScene().addEventHandler(MouseEvent.MOUSE_MOVED, e-> {
            changeRoadHorizonPoint((int)e.getX());
        });
        Main.ROOT.getChildren().add(GRP_CONSTRUCTOR);
        Main.ROOT.getChildren().add(GRP_DISPLAY);
        
    }
    
    public static void update(){
        if(Animation.timerNow - lastUpdate >= 100_000_000){
            updateNumber++;
            lastUpdate = Animation.timerNow;
            Line newMarker = new Line(0, horizonYPoint, Main.WINDOW_WIDTH, horizonYPoint);
            segmentDividers.add(newMarker);
            GRP_CONSTRUCTOR.getChildren().add(newMarker);
            segmentDividersSpeed.add(0.5);
            
            String grassColour;
            if(updateNumber % 2 == 0){
                // Light Colours
                grassColour = "#019512";
            } else {
                // Dark Colours
                grassColour = "#187F1E";
            }
        }
        
        for(int i = 0; i < segmentDividers.size(); i++){
            if(segmentDividers.get(i).getStartY() > Main.WINDOW_HEIGHT*2){
                if(GRP_CONSTRUCTOR.getChildren().contains(segmentDividers.get(i))){
                  GRP_CONSTRUCTOR.getChildren().remove(segmentDividers.get(i));  
                }
            } else {
                int newY = (int) (segmentDividers.get(i).getStartY() + segmentDividersSpeed.get(i));
                segmentDividers.get(i).setStartY(newY);
                segmentDividers.get(i).setEndY(newY);
                segmentDividersSpeed.set(i, segmentDividersSpeed.get(i) + 0.25);
            }
            
            
            
        }
    }
    
    private static void createRoadConstructors(){
        horizonLine = new Line(0, horizonYPoint, Main.WINDOW_WIDTH, horizonYPoint);
        GRP_CONSTRUCTOR.getChildren().add(horizonLine);
        for(int i = 0; i < roadSegments; i++){
            Line newRoadConstructor = new Line();
            roadConstructorLines.add(newRoadConstructor);
            GRP_CONSTRUCTOR.getChildren().add(newRoadConstructor);
            
            Polygon newPoly = new Polygon();
            roadStructurePolys.add(newPoly);
            GRP_CONSTRUCTOR.getChildren().add(newPoly);
            
            Polygon roadKerbLeft = new Polygon();
            roadKerbPolysLeft.add(roadKerbLeft);
            GRP_CONSTRUCTOR.getChildren().add(roadKerbLeft);
            
            Polygon roadKerbRight = new Polygon();
            roadKerbPolysRight.add(roadKerbRight);
            GRP_CONSTRUCTOR.getChildren().add(roadKerbRight);
            
            grassPolyLeft = new Polygon();
            grassPolyLeft.setFill(Color.web("#019512"));
            GRP_DISPLAY.getChildren().add(grassPolyLeft);
        }
    }
    
    public static void changeRoadHorizonPoint(int roadHorizonX){
        roadXAtHorizon = roadHorizonX;
        horizonLine.setStartY(horizonYPoint);
        horizonLine.setEndY(horizonYPoint);
        for(int i = 0; i < roadSegments; i++){
            int startX = roadHorizonX + (((roadStartX - roadHorizonX)/4)*i);
            int endX =  roadHorizonX + (((roadStartX - roadHorizonX)/4)*(i+1));
            int startY = (int) (horizonYPoint + ((375/15) * (Math.pow(2, i))) - (375/15));
            int endY = (int) (horizonYPoint + ((375/15) * (Math.pow(2, i+1))) - (375/15));
            roadConstructorLines.get(i).setStartX(startX);
            roadConstructorLines.get(i).setEndX(endX);
            roadConstructorLines.get(i).setStartY(startY);
            roadConstructorLines.get(i).setEndY(endY);
            
            GRP_CONSTRUCTOR.getChildren().remove(roadStructurePolys.get(i));
            roadStructurePolys.set(i, drawQuad(GRP_CONSTRUCTOR, Color.TRANSPARENT, Color.BLACK,
                    startX + ((375/15) * (Math.pow(2, i))), startY,
                    startX - ((375/15) * (Math.pow(2, i))), startY,
                    endX - ((375/15) * (Math.pow(2, i+1))), endY,
                    endX + ((375/15) * (Math.pow(2, i+1))), endY));
            
            
            int kerbThicknessDivider = 10;
            
            GRP_CONSTRUCTOR.getChildren().remove(roadKerbPolysLeft.get(i));
            roadKerbPolysLeft.set(i, drawQuad(GRP_CONSTRUCTOR, Color.TRANSPARENT, Color.BLACK,
                    (startX - ((375/15) * (Math.pow(2, i)))) + ((startX - (startX - ((375/15) * (Math.pow(2, i)))))/kerbThicknessDivider), startY,
                    (startX - ((375/15) * (Math.pow(2, i)))) - ((startX - (startX - ((375/15) * (Math.pow(2, i)))))/kerbThicknessDivider), startY,
                    (endX - ((375/15) * (Math.pow(2, i+1)))) - ((startX - (startX - ((375/15) * (Math.pow(2, i+1)))))/kerbThicknessDivider), endY,
                    (endX - ((375/15) * (Math.pow(2, i+1)))) + ((startX - (startX - ((375/15) * (Math.pow(2, i+1)))))/kerbThicknessDivider), endY));
            
            grassPolyLeft.getPoints().setAll(0.0, horizonYPoint + 0.0,
                    (getStartXatI(0) - ((375/15) * (Math.pow(2, 0)))) - ((getStartXatI(0) - (getStartXatI(0) - ((375/15) * (Math.pow(2, 0)))))/kerbThicknessDivider), getStartYatI(0) + 0.0,
                    (getStartXatI(1) - ((375/15) * (Math.pow(2, 1)))) - ((getStartXatI(1) - (getStartXatI(1) - ((375/15) * (Math.pow(2, 1)))))/kerbThicknessDivider), getStartYatI(1) + 0.0,
                    (getStartXatI(2) - ((375/15) * (Math.pow(2, 2)))) - ((getStartXatI(2) - (getStartXatI(2) - ((375/15) * (Math.pow(2, 2)))))/kerbThicknessDivider), getStartYatI(2) + 0.0,
                    (getStartXatI(3) - ((375/15) * (Math.pow(2, 3)))) - ((getStartXatI(3) - (getStartXatI(3) - ((375/15) * (Math.pow(2, 3)))))/kerbThicknessDivider), getStartYatI(3) + 0.0,
                    (getStartXatI(4) - ((375/15) * (Math.pow(2, 4)))) - ((getStartXatI(4) - (getStartXatI(4) - ((375/15) * (Math.pow(2, 4)))))/kerbThicknessDivider), getStartYatI(4) + 0.0,
                    0.0, Main.WINDOW_HEIGHT + 0.0);
            
            GRP_CONSTRUCTOR.getChildren().remove(roadKerbPolysRight.get(i));
            roadKerbPolysRight.set(i, drawQuad(GRP_CONSTRUCTOR, Color.TRANSPARENT, Color.BLACK,
                    (startX + ((375/15) * (Math.pow(2, i)))) + ((startX - (startX - ((375/15) * (Math.pow(2, i)))))/kerbThicknessDivider), startY,
                    (startX + ((375/15) * (Math.pow(2, i)))) - ((startX - (startX - ((375/15) * (Math.pow(2, i)))))/kerbThicknessDivider), startY,
                    (endX + ((375/15) * (Math.pow(2, i+1)))) - ((startX - (startX - ((375/15) * (Math.pow(2, i+1)))))/kerbThicknessDivider), endY,
                    (endX + ((375/15) * (Math.pow(2, i+1)))) + ((startX - (startX - ((375/15) * (Math.pow(2, i+1)))))/kerbThicknessDivider), endY));
        }
    }
    
    private static Polygon drawQuad(Group group, Color fillColor, Color strokeColor, double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
        Polygon newPoly = new Polygon();
        newPoly.getPoints().addAll(new Double[]{
            x1, y1,
            x2, y2,
            x3, y3,
            x4, y4 });
        newPoly.setStroke(strokeColor);
        newPoly.setFill(fillColor);
        group.getChildren().add(newPoly);
        return newPoly;
    }
    
    public static int getStartXatI(int i){
        return (roadXAtHorizon + (((roadStartX - roadXAtHorizon)/4)*i));
    }
    
    public static int getStartYatI(int i){
        return  (int) (horizonYPoint + ((375/15) * (Math.pow(2, i))) - (375/15));
    }
    
}
