package general.rendering;

import java.util.ArrayList;

public class LightSource{
    
    public Tile centerTile;
    private final LevelGenerator lGen;
    
    private final ArrayList<Tile> litTiles;
    
    public Character followChar = null;
    
    public LightSource(LevelGenerator lGen){
        this.lGen = lGen;
        litTiles = new ArrayList();
    }
    
    LightSource ring = null;
    public void createLightRing(Tile centerTile, double thisRadius, double ringRadius){
        this.centerTile = centerTile;
        lightTilesInRadius(thisRadius, 0.4);
        if(ring == null) ring = new LightSource(lGen);
        ring.centerTile = centerTile;
        ring.lightTilesInRadius(ringRadius, 0.6);
    }
    
    public void lightTilesInRadius(double radius, double level){
        // Using bounding box method to draw a circle
        int bBoxTopMax = (int) Math.round(centerTile.gy - radius);
        int bBoxBotMax = (int) Math.round(centerTile.gy + radius);
        int bBoxLeftMax = (int) Math.round(centerTile.gx - radius);
        int bBoxRightMax = (int) Math.round(centerTile.gx + radius);
        
        int bBoxTop = (bBoxTopMax < 0 ? 0 : bBoxTopMax);
        int bBoxBot = (bBoxBotMax > lGen.hTiles ? lGen.hTiles : bBoxBotMax);
        int bBoxLeft = (bBoxLeftMax < 0 ? 0 : bBoxLeftMax);
        int bBoxRight = (bBoxRightMax > lGen.wTiles ? lGen.wTiles : bBoxRightMax);
        ArrayList litTileCheck = new ArrayList();
        for (int ty = bBoxTop; ty < bBoxBot; ty++) {
            for (int tx = bBoxLeft; tx < bBoxRight; tx++) {
                
                Tile t = lGen.getTileAt(tx, ty);
                double dx = centerTile.gx - t.gx;
                double dy = centerTile.gy - t.gy;
                double distanceSquared = Math.pow(dx, 2) + Math.pow(dy, 2);
                if(distanceSquared <= Math.pow(radius, 2)){
                    litTileCheck.add(t);
                    if(!litTiles.contains(t)){
                        litTiles.add(t);
                        if(!t.litByLightSourceList.contains(this)){
                            t.addToLightLevel(level);
                            t.litByLightSourceList.add(this);
                        }
                    }
                }
            }
        }
        
        litTiles.forEach(t ->{
            if(!litTileCheck.contains(t)){
                litTileCheck.add(t);
                if(t.litByLightSourceList.contains(this)){
                    t.addToLightLevel(-level);
                    t.litByLightSourceList.remove(this);
                }
            } else {
                litTileCheck.remove(t);
            }
        });
        litTiles.removeAll(litTileCheck);
        
    }
    
}
