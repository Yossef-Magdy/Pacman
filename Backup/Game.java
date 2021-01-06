
import com.sun.opengl.util.FPSAnimator;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
/**
 *
 * @author Yossef
 */
public class Game extends JFrame implements GLEventListener, KeyListener {
    
    private final GLCanvas canvas;
    private final FPSAnimator animate;
    private final Pacman pacman;
    private final int boardWidth, boardHeight, screenScale, frames;
    private final RedGhost redGhost;
    private char direction;
    private int counter;
    String assetsFolderName = "Assets";
    String textureName = "map.png";
    TextureReader.Texture texture;
    int textureIndex[] = new int[1];
    public static void main(String[] args) {
        Runnable run = () -> {
            Game game = new Game();
            game.ini(game);
        };
        EventQueue.invokeLater(run);
    }

    public Game() {
        canvas = new GLCanvas();
        pacman = new Pacman();
        frames = 60;
        animate = new FPSAnimator(frames);
        animate.add(canvas);
        boardWidth = 28;
        boardHeight = 36;
        screenScale = 20;
        pacman.setSpeed(0.8);
        redGhost = new RedGhost();
        redGhost.setX(20);
        redGhost.setY(20);
        redGhost.setSpeed(0.5);
    }

    public void ini(Game game) {
        this.setSize(screenScale * boardWidth, screenScale * boardHeight);
        this.setVisible(true);
        this.center();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Pacman");
        canvas.addGLEventListener(game);
        canvas.addKeyListener(this);
        animate.start();
        this.add(canvas);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE20);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(1, textureIndex, 0);
         try {
            texture = TextureReader.readTexture(assetsFolderName + "/" + textureName , true);
            gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[0]);

//                mipmapsFromPNG(gl, new GLU(), texture[i]);
            new GLU().gluBuild2DMipmaps(
                GL.GL_TEXTURE_2D,
                GL.GL_RGBA, // Internal Texel Format,
                texture.getWidth(), texture.getHeight(),
                GL.GL_RGBA, // External format from image,
                GL.GL_UNSIGNED_BYTE,
                texture.getPixels() // Imagedata
                );
        } catch( IOException e ) {
          System.out.println(e);
          e.printStackTrace();
        }
        gl.glViewport(0, boardWidth, 0, boardHeight);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, boardWidth, 0, boardHeight, -1.0, 1.0);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        counter++;
        if(counter > 1000) {
            counter = 0;
        }
        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        drawMap(gl);
        pacman.draw(gl, screenScale);
        redGhost.draw(gl);
        move(gl);
    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {

    }

    @Override
    public void displayChanged(GLAutoDrawable glad, boolean bln, boolean bln1) {

    }
    public void move(GL gl) {
        if(counter % (int)(1 / redGhost.getSpeed()) == 0) {
            redGhost.chase((int)pacman.getX(), (int)pacman.getY());
            //redGhost.frightened();
        }
        
        if(pacman.getX() == redGhost.getX() && pacman.getY() == redGhost.getY()) {
            //animate.stop();
            System.out.println("YOU LOSE");
        }
        switch (direction) {
            case 'w': {
                pacman.incrementY();
                pacman.setY(pacman.getY() % (boardHeight - 5));
                break;
            }
            case 's': {
                pacman.decrementY();
                pacman.setY((boardHeight - 5 + pacman.getY()) % (boardHeight - 5));
                break;
            }
            case 'd': {
                pacman.incrementX();
                pacman.setX(pacman.getX() % boardWidth);
                break;
            }
            case 'a': {
                pacman.decrementX();
                pacman.setX((boardWidth + pacman.getX()) % boardWidth);
                break;
            }
        }
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

    public void drawMap(GL gl) {
        gl.glEnable(GL.GL_BLEND);	// Turn Blending On
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[0]);	
        gl.glColor3d(0.5, 0.36, 0);
        gl.glBegin(GL.GL_QUADS);
        //gl.glTexCoord2d(0, 0);
        gl.glVertex2i(0, 2);
        //gl.glTexCoord2d(0, 1);
        gl.glVertex2i(0, boardHeight - 3);
        //gl.glTexCoord2d(1, 1);
        gl.glVertex2i(boardWidth, boardHeight - 3);
        //gl.glTexCoord2d(1, 0);
        gl.glVertex2i(boardWidth, 2);
        gl.glEnd();
        gl.glColor3d(0, 0, 0);
        gl.glDisable(GL.GL_BLEND);
    }

    public void drawGrid(GL gl) {
        for (int i = 0; i < boardHeight; i++) {
            gl.glColor3d(1, 0, 0);
            gl.glBegin(GL.GL_LINE_STRIP);
            gl.glVertex2i(0, i);
            gl.glVertex2i(boardWidth, i);
            gl.glEnd();
        }

        for (int i = 0; i < boardWidth; i++) {
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
        char c = e.getKeyChar();
        if(c == 'w' || c == 's' || c == 'd' || c == 'a' || c == 'p') {
            direction = e.getKeyChar();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
