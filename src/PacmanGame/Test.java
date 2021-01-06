package PacmanGame;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.media.opengl.*;

/**
 *
 * @author Yossef
 */
public class Test extends JFrame implements GLEventListener, KeyListener {
    private final GLCanvas canvas;
    private Test test;
    private int x, y, deg;
    private final int boardWidth;
    private final int boardHeight;
    private final int screenScale;
    public static void main(String[] args) {
        Runnable run = () -> {
            Test test = new Test();
            test.set(test);
            test.initiate();
        };
        EventQueue.invokeLater(run);
    }
    public void set(Test test) {
        this.test = test;
    }
    public Test() {
        canvas = new GLCanvas();
        boardWidth = 28;
        boardHeight = 36;
        screenScale = 20;
        x = 10;
        y = 1;
    }
    public void initiate() {
        this.setSize(screenScale * boardWidth, screenScale * boardHeight);
        this.setVisible(true);
        this.center();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Pacman");
        canvas.addGLEventListener(this.test);
        canvas.addKeyListener(this);
        this.add(canvas);
    }
    @Override
    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE20);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glViewport(0, boardWidth, 0, boardHeight);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, boardWidth, 0, boardHeight, -1.0, 1.0);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        drawMap(gl);
        drawGrid(gl);
        drawPlayer(gl);
        drawMsg(gl);
    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {

    }

    @Override
    public void displayChanged(GLAutoDrawable glad, boolean bln, boolean bln1) {

    }
    public void center() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        this.setLocation(
                (screenSize.width - frameSize.width) >> 1,
                (screenSize.height - frameSize.height) >> 1
        );
    }
    public void drawPlayer(GL gl) {
        gl.glColor3f(0, 1, 0);
        gl.glPushMatrix();
            double X = x, Y = y;
            gl.glBegin(GL.GL_QUADS);
            gl.glVertex2d(X, Y);
            gl.glVertex2d(X, Y+1);
            gl.glVertex2d(X+1, Y+1);
            gl.glVertex2d(X+1, Y);
            gl.glEnd();
        gl.glPopMatrix();
    }
    public void drawMsg(GL gl) {
        gl.glBegin(GL.GL_QUADS);
        /*gl.glVertex2i(10, 20);
        gl.glVertex2i(this.getWidth()/2-150, this.getHeight()/2);
        gl.glVertex2i(this.getWidth()/2, this.getHeight()/2);
        gl.glVertex2i(this.getWidth()/2, this.getHeight()/2-150);
        */
        gl.glEnd();
    }
    public void drawMap(GL gl) {
        gl.glColor3d(0.5, 0.36, 0);
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex2i(0, 2);
        gl.glVertex2i(0, boardHeight - 3);
        gl.glVertex2i(boardWidth, boardHeight - 3);
        gl.glVertex2i(boardWidth, 2);
        gl.glEnd();
        gl.glColor3d(0, 0, 0);
    }
    public void drawGrid(GL gl) {
        for(int i = 0; i < boardHeight; i++) {
            gl.glColor3d(1, 0, 0);
            gl.glBegin(GL.GL_LINE_STRIP);
            gl.glVertex2i(0, i);
            gl.glVertex2i(boardWidth, i);
            gl.glEnd();
        }
        
        for(int i = 0; i < boardWidth; i++) {
            gl.glColor3d(1, 0, 1);
            gl.glBegin(GL.GL_LINE_STRIP);
            gl.glVertex2i(i, 0);
            gl.glVertex2i(i, boardHeight);
            gl.glEnd();
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        char ch = e.getKeyChar();
        if (ch == 'w') {
            y += 2;
        }
        if (ch == 'd') {
            x += 2;
        }
        if (ch == 'a') {
            x -= 2;
        }
        if (ch == 's') {
            y -= 2;
        }
        if(ch == '+') {
            deg++;
        }
        if(ch == '-') {
            deg--;
        }
        canvas.display();
        if(ch == 'p') 
        System.out.println("(" + x + ", " + y + ")");
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
