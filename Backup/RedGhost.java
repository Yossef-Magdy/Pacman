
import com.sun.opengl.util.FPSAnimator;
import java.util.ArrayList;
import javax.media.opengl.GL;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Yossef
 */
public class RedGhost extends Ghost{
    private ArrayList<Pair> path;
    public RedGhost() {
        this.path = new ArrayList<>();
    }
    @Override
    public void chase(int x, int y) {
        int curX = (int)super.getX();
        int curY = (int)super.getY();
        int grid[][] = new int[28][36];
        FindPath finder = new FindPath(grid);
        this.path = finder.find(curX, curY, x, y);
        if(!path.isEmpty()) {
            path.remove(0);
            nextTile();
        }
        
    }
    public void nextTile() {
        if(!path.isEmpty()) {
            super.setX(path.get(0).x);
            super.setY(path.get(0).y);
        }
    }
    @Override
    public void frightened() {
        
       
    }

    @Override
    public void draw(GL gl) {
        int size = 20;
        gl.glPointSize(size);
        gl.glColor3f(1, 0, 0);
        gl.glBegin(GL.GL_POINTS);
        gl.glVertex2d(super.getX() + 0.5, super.getY() + 0.5 + 2);
        gl.glEnd();
    }
    
}
