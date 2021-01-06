package PacmanGame;
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
public class BlueGhost extends Ghost{
    ArrayList<Pair> path;
    ArrayList<Pair> pathToRightLowerCorner;
    public BlueGhost() {
        super.setX(16);
        super.setY(16);
        super.setUpIndex(25);
        super.setLeftIndex(23);
        super.setRightIndex(24);
        super.setDownIndex(22);
        super.setCurrentIndex(super.getRightIndex());
        super.getIn();
    }
    
    @Override
    public void chase(int pacmanX, int pacmanY, int grid[][]) {
        if(!super.isOut()) {
            return;
        }
        int curX = (int)super.getX();
        int curY = (int)super.getY();
        FindPath finder = new FindPath(grid);
        this.path = finder.find(curX, curY, pacmanX, pacmanY);
        nextTile(path);
    }

    @Override
    public void frightened(int grid[][]) {
        
    }

    @Override
    public void draw(GL gl, int textures[]) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[super.getCurrentIndex()]);	// Turn Blending On
        gl.glPushMatrix();
            gl.glBegin(GL.GL_QUADS);
                gl.glTexCoord2f(0.0f, 0.0f);
                gl.glVertex2d(Math.floor(super.getX()), Math.floor(super.getY()) + 2);
                gl.glTexCoord2f(0.0f, 1.0f);
                gl.glVertex2d(Math.floor(super.getX()), Math.floor(super.getY()) + 3);
                gl.glTexCoord2f(1.0f, 1.0f);
                gl.glVertex2d(Math.floor(super.getX()) + 1, Math.floor(super.getY()) + 3);
                gl.glTexCoord2f(1.0f, 0.0f);
                gl.glVertex2d(Math.floor(super.getX()) + 1, Math.floor(super.getY()) + 2);
            gl.glEnd();
        gl.glPopMatrix();
        
        gl.glDisable(GL.GL_BLEND);
    }

    @Override
    public void scatter(int x, int y, int[][] grid) {
        
    }
}
