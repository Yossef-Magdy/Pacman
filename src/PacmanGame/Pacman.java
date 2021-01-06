package PacmanGame;
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
public class Pacman extends Entity {
    private boolean alive;
    private int lives;
    private int frameIndex;
    private int deathIndex;
    private int score;
    public Pacman() {
        super.setX(13);
        super.setY(7);
        super.setSpeed(0.5);
        alive = true;
        lives = 3;
    }
    public int getLives() {
        return lives;
    }
    public void resetLives() {
        lives = 3;
    }
    public void reserScore() {
        score = 0;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score += score;
    }
    public void increaseScore() {
        score += 10;
    }
    public void nextFrame() {
        frameIndex++;
        frameIndex %= 3;
    }
    public boolean nextDeathFrame() {
        deathIndex++;
        deathIndex %= 6;
        if(deathIndex == 5) {
            revive();
            deathIndex = 0;
            lives--;
            return true;
        }
        return false;
    }
    public boolean isDead() {
        return !alive;
    }
    public void die() {
        alive = false;
    }
    public int getDeathIndex() {
        return deathIndex;
    }
    public void revive() {
        alive = true;
    }
    public void incrementX(int grid[][]) {
        if(valid(grid, super.getX()  + super.getSpeed(), super.getY()))
        super.setX(super.getX() + super.getSpeed());
    }

    public void decrementX(int grid[][]) {
        if(valid(grid, super.getX()  - super.getSpeed(), super.getY()))
        super.setX(super.getX() - super.getSpeed());
    }

    public void incrementY(int grid[][]) {
        if(valid(grid, super.getX(), super.getY() + super.getSpeed()))
        super.setY(super.getY() + super.getSpeed());
    }

    public void decrementY(int grid[][]) {
        if(valid(grid, super.getX(), super.getY() - super.getSpeed()))
        super.setY(super.getY() - super.getSpeed());
    }
    private boolean valid(int grid[][], double x, double y) {
        int X = (int)x;
        int Y = (int)y;
        if(x == -1 && y == 16) {
            return true;
        }
        boolean validExp = (0 <= X && X < grid.length) && (0 <= Y && Y < grid[0].length);
        if(validExp) {
            return grid[X][Y] != 1;
        }
        else {
            return false;
        }
    }
    public void draw(GL gl, int textures[], int offset) {
        gl.glEnable(GL.GL_BLEND);
        if(isDead()) {
            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[offset + deathIndex]);
        }
        else {
            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[offset + frameIndex]);
        }
        gl.glPushMatrix();
        //gl.glTranslated(0, 0 - 1.25, 0);
//            gl.glScaled(1.2, 1.2,1);
//            double X = super.getX() / 1.2;
//            double Y = super.getY() / 1.2;
            
            int X = (int)super.getX();
            int Y = (int)super.getY();
            gl.glBegin(GL.GL_QUADS);
                gl.glTexCoord2f(0.0f, 0.0f);
                gl.glVertex2d(X, Y + 2);
                gl.glTexCoord2f(0.0f, 1.0f);
                gl.glVertex2d(X, Y + 3);
                gl.glTexCoord2f(1.0f, 1.0f);
                gl.glVertex2d(X + 1, Y + 3);
                gl.glTexCoord2f(1.0f, 0.0f);
                gl.glVertex2d(X + 1, Y + 2);
            gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }
}
