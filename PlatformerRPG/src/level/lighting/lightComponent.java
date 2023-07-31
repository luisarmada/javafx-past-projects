package level.lighting;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import javafx.scene.Node;

public class lightComponent {
    
    int radius = 5;
    int x, y;
    Node comp = new Node() {
        @Override
        protected NGNode impl_createPeer() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        protected boolean impl_computeContains(double localX, double localY) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };
    
    public lightComponent(int startX, int startY){
        comp.setLayoutX(startX);
        comp.setLayoutY(startY);
        lightBlockManager.lComps.add(comp);
    }
    
    public void setLocation(int newx, int newy){
        x = newx; y = newy;
    }
    
}
