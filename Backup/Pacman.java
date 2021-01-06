
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

    public void incrementX() {
        super.setX(super.getX() + super.getSpeed());
    }

    public void decrementX() {
        super.setX(super.getX() - super.getSpeed());
    }

    public void incrementY() {
        super.setY(super.getY() + super.getSpeed());
    }

    public void decrementY() {
        super.setY(super.getY() - super.getSpeed());
    }

    public void draw(GL gl, int size) {
        gl.glPointSize(size);
        gl.glColor3f(0, 1, 0);
        gl.glBegin(GL.GL_POINTS);
        gl.glVertex2d(Math.floor(super.getX()) + 0.5, Math.floor(super.getY()) + 0.5 + 2);
        gl.glEnd();
    }
}
