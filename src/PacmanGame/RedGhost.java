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
public class RedGhost extends Ghost{
    private ArrayList<Pair> path;
    public RedGhost() {
        this.path = new ArrayList<>();
        super.setX(14);
        super.setY(19);
        super.setUpIndex(21);
        super.setLeftIndex(19);
        super.setRightIndex(20);
        super.setDownIndex(18);
        super.setCurrentIndex(super.getRightIndex());
    }
    @Override
    public void chase(int pacmanX, int pacmanY, int grid[][]) {
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
    public void getOutOfTheBox() {}

    @Override
    public void scatter(int x, int y, int[][] grid) {
        
    }
    
}
