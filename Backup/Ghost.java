
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
public abstract class Ghost extends Entity {

    public Ghost() {
        
    }

    public void scatter() {
        //move the ghosts randomly
    }

    public abstract void chase(int x, int y);

    public abstract void frightened();
    
    public abstract void draw(GL gl);
}
