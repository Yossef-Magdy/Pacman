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
public abstract class Ghost extends Entity {
    private int up_index, down_index, left_index, right_index, currentIndex;
    private boolean out;
    public Ghost() {
        
    }
    public boolean isOut() {
        return out;
    }
    public void getIn() {
        out = false;
    }
    public void getOut() {
        out = true;
    }
    public int getUpIndex() {
        return up_index;
    }

    public int getDownIndex() {
        return down_index;
    }

    public int getLeftIndex() {
        return left_index;
    }

    public int getRightIndex() {
        return right_index;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
    public void setUpIndex(int index) {
        up_index = index;
    }
    public void setDownIndex(int index) {
        down_index = index;
    }
    public void setRightIndex(int index) {
        right_index = index;
    }
    public void setLeftIndex(int index) {
        left_index = index;
    }
    public void setCurrentIndex(int index) {
        currentIndex = index;
    }
    public void nextTile(ArrayList<Pair> path) {
        if(!path.isEmpty()) {
            path.remove(0);
            if(path.size() > 1) {
                int diffX = path.get(1).x - path.get(0).x;
                int diffY = path.get(1).y - path.get(0).y;
                if(diffX == 1) {
                    setCurrentIndex(getRightIndex());
                }
                else if(diffX == -1) {
                    setCurrentIndex(getLeftIndex());
                }
                if(diffY == 1) {
                    setCurrentIndex(getUpIndex());
                }
                else if(diffY == -1) {
                    setCurrentIndex(getDownIndex());
                }
            }
            if(path.size() > 0) {
                super.setX(path.get(0).x);
                super.setY(path.get(0).y);
            }
        }
    }
    public boolean valid(int grid[][], double x, double y) {
        int X = (int)x;
        int Y = (int)y;
        boolean validExp = (0 <= X && X < grid.length) && (0 <= Y && Y < grid[0].length);
        if(validExp) {
            return grid[X][Y] != 1;
        }
        else {
            return false;
        }
    }
    
    public void getOutOfTheBox() {
        super.setY(super.getY() + 3);
        getOut();
    }
    public abstract void scatter(int x, int y, int grid[][]);
    
    public abstract void chase(int x, int y, int grid[][]);

    public abstract void frightened(int grid[][]);
    
    public abstract void draw(GL gl, int textures[]);
}
