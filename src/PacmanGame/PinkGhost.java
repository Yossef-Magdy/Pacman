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
public class PinkGhost extends Ghost{
    private ArrayList<Pair> path;
    ArrayList<Pair> pathToLeftUpperCorner;
    private char pacmanDirection;
    public PinkGhost() {
        super.setX(12);
        super.setY(16);
        super.setUpIndex(33);
        super.setLeftIndex(31);
        super.setRightIndex(32);
        super.setDownIndex(30);
        super.setCurrentIndex(super.getRightIndex());
        super.getIn();
        pacmanDirection = 'd';
        path = new ArrayList<>();
    }
    public void setPacmanDirection(char direction) {
        pacmanDirection = direction;
    }
    @Override
    public void chase(int pacmanX, int pacmanY, int grid[][]) {
        if(!super.isOut()) {
            return;
        }
        int curX = (int)super.getX();
        int curY = (int)super.getY();
        switch(pacmanDirection) {
            case 'd' : {
                pacmanX += 4;
                break;
            }
            case 'a' : {
                pacmanX -= 4;
            }
            case 'w' : {
                pacmanY += 4;
            }
            case 's' : {
                pacmanY -= 4;
            }
        }
        
        FindPath finder = new FindPath(grid);
        if(valid(grid, pacmanX, pacmanY)) {
            this.path = finder.find(curX, curY, pacmanX, pacmanY);
            nextTile(path);
        }
        else {
            this.pathToLeftUpperCorner = finder.find(curX, curY, 1, 28);
            nextTile(pathToLeftUpperCorner);
        }
        
        
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
