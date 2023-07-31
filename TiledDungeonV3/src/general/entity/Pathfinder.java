package general.entity;

import general.rendering.Tile;
import general.rendering.LevelDrawer;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.paint.Color;

public class Pathfinder {
    
    ArrayList<Tile> openList = new ArrayList();
    ArrayList<Tile> closedList = new ArrayList();
    ArrayList<Integer> gCostList = new ArrayList();
    ArrayList<Integer> hCostList = new ArrayList();
    ArrayList<Tile> fromTileList = new ArrayList();
    
    public ArrayList<Tile> pathfind(Tile sTile, Tile gTile, LevelDrawer lDrawer, boolean draw){
        openList.clear();
        closedList.clear();
        gCostList.clear();
        hCostList.clear();
        fromTileList.clear();
        
        if(sTile.tState != 0 || gTile.tState != 0){
            System.out.println("ERROR: PATHFINDING TO WALL TILES");
            return null;
        }
        
        openList.add(sTile);
        gCostList.add(0);
        hCostList.add(0);
        fromTileList.add(null);
        do{
            
            int bestTileIndex = bestTileIndex();
            Tile currentTile = openList.get(bestTileIndex);
            closedList.add(currentTile);
            
            if(draw){
                lDrawer.addDrawTiles(-1, -1, Color.web("#CB4335"), new ArrayList<>(Arrays.asList(currentTile)));
            }
            
            if(closedList.contains(gTile)){
                break;
            }
            
            for (int i = 0; i < currentTile.adjTileList.size(); i++) {
                Tile adjTile = currentTile.adjTileList.get(i);
                if(closedList.contains(adjTile) || adjTile.tState != 0){
                    continue;
                }
                
                if(!openList.contains(adjTile)){
                    openList.add(adjTile);
                    fromTileList.add(currentTile);
                    gCostList.add(calcGCost(currentTile));
                    hCostList.add(Math.abs(adjTile.gx - gTile.gx) + Math.abs(adjTile.gy - gTile.gy));
                } else {
                    int newGCost = calcGCost(currentTile);
                    if(newGCost < gCostList.get(openList.indexOf(adjTile))){
                        gCostList.set(openList.indexOf(adjTile), newGCost);
                        fromTileList.set(openList.indexOf(adjTile), currentTile);
                    }
                    
                }
            }
        } while(!openList.isEmpty());
        
        ArrayList<Tile> pathTiles = new ArrayList();
        
        pathTiles.add(gTile);
        
        Tile nextTile = fromTileList.get(fromTileList.size() - 1);
        do{
            pathTiles.add(nextTile);
            nextTile = fromTileList.get(openList.indexOf(nextTile));
        } while(nextTile != null);
        
        return pathTiles;
    }
   
    private Integer bestTileIndex(){
        int lowestFCost = 99999;
        ArrayList<Integer> bestTileList = new ArrayList();
        
        for(int i = 0; i < openList.size(); i++) {
            if(closedList.contains(openList.get(i))){
                continue;
            }
            int fCost = gCostList.get(i) + hCostList.get(i);
            if(fCost < lowestFCost){
                lowestFCost = fCost;
                bestTileList.clear();
                bestTileList.add(i);
            } else if(fCost == lowestFCost){
                bestTileList.add(i);
            }
        }
        
        return bestTileList.get(bestTileList.size() - 1);
    }
    
    private int calcGCost(Tile t){
        int gCost = gCostList.get(openList.indexOf(t)) + 1;
        return gCost;
    }
    
}
