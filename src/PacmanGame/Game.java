package PacmanGame;

import com.sun.opengl.util.FPSAnimator;
import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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
    private final OrangeGhost orangeGhost;
    private final BlueGhost blueGhost;
    private final PinkGhost pinkGhost;
    private char direction;
    private int counter;
    private int seconds;
    private final int grid[][];
    private Clip clip;
    static int points;
    private boolean gameOver;
    private boolean gameCompleted;
    private final JTextField playerName;
    private TextRenderer textWriter, gameStateWriter;
    private final Menu menu;
    private final Input inputDialog;
    private final JPanel menuPanel;
    private final JPanel mainPanel;
    private final HowToPlayPage howToPlayPage;
    private ScoresPage scoresPage;
    private final JButton play;
    private final JButton save, cancel, howToPlay, scores;
    private CardLayout cardController;
    private boolean deathSoundIsPlayed, munchSoundIsPlayed, gameStarted;
    private final File obsFile, pointsFile, score;
    private final String assetsFolderName = "Assets";
    private final String textureNames[] = {"left1.png", "left2.png", "left3.png",
        "right1.png", "right2.png", "right3.png",
        "up1.png", "up2.png", "up3.png",
        "down1.png", "down2.png", "down3.png",
        "die1.png", "die2.png", "die3.png", "die4.png", "die5.png", "map.png",
        "red_down.png", "red_left.png", "red_right.png", "red_up.png",
        "blue_down.png", "blue_left.png", "blue_right.png", "blue_up.png",
        "orange_down.png", "orange_left.png", "orange_right.png", "orange_up.png",
        "pink_down.png", "pink_left.png", "pink_right.png", "pink_up.png", "point.png", "megaPoint.png", "lives.png", "msg.png"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textureIndex[] = new int[textureNames.length];
    ArrayList<Integer> blocked;

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
        frames = 15;
        animate = new FPSAnimator(frames);
        animate.add(canvas);
        boardWidth = 28;
        boardHeight = 36;
        screenScale = 20;
        redGhost = new RedGhost();
        orangeGhost = new OrangeGhost();
        blueGhost = new BlueGhost();
        pinkGhost = new PinkGhost();
        blocked = new ArrayList<>();
        grid = new int[boardWidth][boardHeight];
        obsFile = new File("blocked.txt");
        pointsFile = new File("points.txt");
        score = new File("score.txt");
        seconds = 1;
        points = 296;
        deathSoundIsPlayed = false;
        gameOver = false;
        gameCompleted = false;
        menu = new Menu();
        menuPanel = menu;
        mainPanel = new JPanel();
        inputDialog = new Input();
        playerName = inputDialog.getPlayerNameField();
        save = inputDialog.getSaveButton();
        cancel = inputDialog.getCancelButton();
        howToPlay = menu.getHowToPlayButton();
        play = menu.getPlayButton();
        scores = menu.getScoresButton();
        howToPlayPage = new HowToPlayPage();
        scoresPage = new ScoresPage();
        gameStarted = false;
    }

    public void playSound(String name) {
        try {
            clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(assetsFolderName + "//" + name).getAbsoluteFile());
            clip.open(inputStream);
            clip.start();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    public void checkGameCompletion() {
        //win
        if (points == 0) {
            gameCompleted = true;
        }
    }

    public void readMap() throws FileNotFoundException {
        try (Scanner read = new Scanner(obsFile)) {
            int readIntegers = 0;
            int x = 0;
            while (read.hasNextInt()) {
                int num = read.nextInt();
                readIntegers++;
                if (readIntegers % 2 == 1) {
                    x = num;
                } else {
                    grid[x][num] = 1;
                }
            }
            read.close();
        }
        try (Scanner read = new Scanner(pointsFile)) {
            int readIntegers = 0;
            int x = 0;
            while (read.hasNextInt()) {
                int num = read.nextInt();
                readIntegers++;
                if (readIntegers % 2 == 1) {
                    x = num;
                } else {
                    grid[x][num] = 2;
                }
            }
            read.close();
        }
    }

    public void ini(Game game) {
        this.setSize(screenScale * boardWidth, screenScale * boardHeight);
        this.setVisible(true);
        this.center();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Pacman");
        this.addKeyListener(this);
        mainPanel.setLayout(new CardLayout());
        this.add(mainPanel);
        mainPanel.add(canvas, "canvas");
        mainPanel.add(menuPanel, "menu");
        cardController = (CardLayout) (mainPanel.getLayout());
        cardController.show(mainPanel, "menu");
        canvas.addGLEventListener(game);
        canvas.addKeyListener(this);
        inputDialog.setSize(415, 250);
        howToPlayPage.setSize(400, 500);
        howToPlayPage.setTitle("How to play");
        play.addActionListener((ActionEvent e) -> {
            cardController.show(mainPanel, "canvas");
            if (!animate.isAnimating()) {
                animate.start();
            }
            clip.stop();
        });
        scores.addActionListener((ActionEvent e) -> {
            try {
                scoresPage.getScores();
                scoresPage.setVisible(true);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        howToPlay.addActionListener((ActionEvent e) -> {
            howToPlayPage.setVisible(true);
        });
        playSound("a.wav");
        try {
            readMap();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        save.addActionListener((ActionEvent e) -> {
            saveScore();
            inputDialog.dispose();
        });
        cancel.addActionListener((ActionEvent e) -> {
            inputDialog.dispose();
        });
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        textWriter = new TextRenderer(new Font("Verdana", Font.BOLD, 16));
        textWriter.setColor(1, 1, 1, 1);
        gameStateWriter = new TextRenderer(new Font("Verdana", Font.BOLD, 36));
        gameStateWriter.setColor(1, 1, 1, 1);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textureIndex, 0);
        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "/" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[i]);

                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA, // Internal Texel Format,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels() // Imagedata
                );
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        gl.glViewport(0, boardWidth, 0, boardHeight);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, boardWidth, 0, boardHeight, -1.0, 1.0);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        counter++;
        counter %= 100;
        if (counter % frames == 0) {
            seconds++;
        }
        checkGameCompletion();
        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        drawMap(gl);
        drawPoints(gl);
        drawLives(gl);
        pacman.nextFrame();
        if (pacman.getX() == 13 && pacman.getY() == 7) {
            pacman.draw(gl, textureIndex, 0);
        }
        redGhost.draw(gl, textureIndex);
        pinkGhost.draw(gl, textureIndex);
        orangeGhost.draw(gl, textureIndex);
        blueGhost.draw(gl, textureIndex);
        if (seconds > 3 || gameStarted) {
            move(gl);
        }

        try {
            textWriter.beginRendering(drawable.getWidth(), drawable.getHeight());
            textWriter.draw(pacman.getScore() + "", 1, drawable.getHeight() - 50);
            textWriter.draw("High Score", drawable.getWidth() / 2 - 50, drawable.getHeight() - 20);
            if (!gameStarted && seconds <= 3) {
                textWriter.draw(3 - seconds + "", drawable.getWidth() - 50, drawable.getHeight() - 50);
            } else {
                gameStarted = true;
            }
            textWriter.endRendering();
            if (gameOver) {
                drawMessage(gl, "Game Over!");
                animate.stop();
                takePlayerName();
            }
            if (gameCompleted) {
                drawMessage(gl, "Congratulations");
                animate.stop();
                takePlayerName();
            }
            if (gameOver || gameCompleted) {
                textWriter.beginRendering(drawable.getWidth(), drawable.getHeight());
                textWriter.draw("Press any key to continue", drawable.getWidth() - (int)gameStateWriter.getBounds("Press any key to continue").getWidth()/2, drawable.getHeight() - 50);
                textWriter.endRendering();
            }
        } catch (Exception e) {

        };
    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {

    }

    @Override
    public void displayChanged(GLAutoDrawable glad, boolean bln, boolean bln1) {

    }

    public void drawMessage(GL gl, String msg) {
        int width = 2 + (int) gameStateWriter.getBounds(msg).getWidth() / screenScale;
        int height = 2 + (int) gameStateWriter.getBounds(msg).getHeight() / screenScale;
        int x = boardWidth / 2 - width / 2;
        int y = boardHeight / 2 - height / 2;
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[37]);
        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2d(0, 0);
        gl.glVertex2i(x, y);
        gl.glTexCoord2d(0, 1);
        gl.glVertex2i(x + width, y);
        gl.glTexCoord2d(1, 1);
        gl.glVertex2i(x + width, y + height);
        gl.glTexCoord2d(1, 0);
        gl.glVertex2i(x, y + height);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
        gameStateWriter.beginRendering(this.getWidth(), this.getHeight());
        gameStateWriter.draw(msg, this.getWidth() / 2 - (int) gameStateWriter.getBounds(msg).getWidth() / 2, this.getHeight() / 2);
        gameStateWriter.endRendering();
    }

    public void drawPoints(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[34]);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 2) {
                    double x = i * 4;
                    double y = j * 4;
                    gl.glPushMatrix();
                    gl.glTranslated(0.25, 2, 0);
                    gl.glScaled(0.25, 0.25, 1);
                    gl.glBegin(GL.GL_QUADS);
                    gl.glTexCoord2f(0.0f, 0.0f);
                    gl.glVertex2d(x, y + 2);
                    gl.glTexCoord2f(0.0f, 1.0f);
                    gl.glVertex2d(x, y + 3);
                    gl.glTexCoord2f(1.0f, 1.0f);
                    gl.glVertex2d(x + 1, y + 3);
                    gl.glTexCoord2f(1.0f, 0.0f);
                    gl.glVertex2d(x + 1, y + 2);
                    gl.glEnd();
                    gl.glPopMatrix();

                }
            }
        }
        gl.glDisable(GL.GL_BLEND);
    }

    public void returnAllToStartingPoint() {
        pacman.setX(13);
        pacman.setY(7);
        redGhost.setX(13);
        redGhost.setY(19);
        pinkGhost.setX(12);
        pinkGhost.setY(16);
        orangeGhost.setX(14);
        orangeGhost.setY(16);
        blueGhost.setX(16);
        blueGhost.setY(16);
    }

    public boolean collided() {
        boolean redCollision = Math.floor(pacman.getX()) == Math.floor(redGhost.getX()) && Math.floor(pacman.getY()) == Math.floor(redGhost.getY());
        boolean blueCollision = Math.floor(pacman.getX()) == Math.floor(blueGhost.getX()) && Math.floor(pacman.getY()) == Math.floor(blueGhost.getY());
        boolean orangeCollision = Math.floor(pacman.getX()) == Math.floor(orangeGhost.getX()) && Math.floor(pacman.getY()) == Math.floor(orangeGhost.getY());
        boolean pinkCollision = Math.floor(pacman.getX()) == Math.floor(pinkGhost.getX()) && Math.floor(pacman.getY()) == Math.floor(pinkGhost.getY());
        if (redCollision || blueCollision || orangeCollision || pinkCollision) {
            seconds = 0;
            blueGhost.getIn();
            orangeGhost.getIn();
            pinkGhost.getIn();
            return true;
        }
        return false;
    }

    public void move(GL gl) {

        if (counter % 4 == 0) {
            redGhost.chase((int) pacman.getX(), (int) pacman.getY(), grid);
            orangeGhost.chase((int) pacman.getX(), (int) pacman.getY(), grid);
            pinkGhost.chase((int) pacman.getX(), (int) pacman.getY(), grid);
            blueGhost.chase((int) pacman.getX(), (int) pacman.getY(), grid);
        }
        if (seconds > 8) {
            if (!pinkGhost.isOut()) {
                pinkGhost.getOutOfTheBox();
            }
        }
        if (seconds > 13) {
            if (!blueGhost.isOut()) {
                blueGhost.getOutOfTheBox();
            }
        }
        if (seconds > 18) {
            if (!orangeGhost.isOut()) {
                orangeGhost.getOutOfTheBox();
            }
        }
        if (collided()) {
            pacman.die();
            if (!deathSoundIsPlayed) {
                playSound("pacman_death.wav");
                deathSoundIsPlayed = true;
            }
        }
        if (pacman.isDead()) {
            pacman.draw(gl, textureIndex, 12);
            if (pacman.nextDeathFrame()) {
                direction = 'D';
                returnAllToStartingPoint();
                deathSoundIsPlayed = false;
            }

        } else {
            switch (direction) {
                case 'w': {
                    pacman.draw(gl, textureIndex, 6);
                    pacman.incrementY(grid);
                    break;
                }
                case 's': {
                    pacman.draw(gl, textureIndex, 9);
                    pacman.decrementY(grid);
                    break;
                }
                case 'd': {
                    pacman.draw(gl, textureIndex, 3);
                    pacman.incrementX(grid);
                    pacman.setX(pacman.getX() % (boardWidth - 1));
                    break;
                }
                case 'a': {
                    pacman.draw(gl, textureIndex, 0);
                    pacman.decrementX(grid);
                    pacman.setX((boardWidth + pacman.getX()) % boardWidth);
                    break;
                }
                case 'D': {
                    pacman.draw(gl, textureIndex, 0);
                }
            }
            if (grid[(int) pacman.getX()][(int) pacman.getY()] == 2) {
                grid[(int) pacman.getX()][(int) pacman.getY()] = 0;
                pacman.increaseScore();
                Game.points--;

                if (!munchSoundIsPlayed) {
                    playSound("pacman_chomp.wav");
                    Timer timer = new Timer(600, (ActionEvent e) -> {
                        munchSoundIsPlayed = false;
                    });
                    timer.setRepeats(false);
                    timer.start();
                    munchSoundIsPlayed = true;
                }
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

    public void drawLives(GL gl) {
        int x = 1, y = 0;
        if (pacman.getLives() == 0) {
            gameOver = true;
        }
        for (int i = 0; i < pacman.getLives(); i++) {
            gl.glEnable(GL.GL_BLEND);
            gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[36]);
            gl.glPushMatrix();
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2d(0, 0);
            gl.glVertex2i(x, y);
            gl.glTexCoord2d(0, 1);
            gl.glVertex2i(x, y + 1);
            gl.glTexCoord2d(1, 1);
            gl.glVertex2i(x + 1, y + 1);
            gl.glTexCoord2d(1, 0);
            gl.glVertex2i(x + 1, y);
            gl.glEnd();
            gl.glPopMatrix();
            gl.glDisable(GL.GL_BLEND);
            x += 2;
        }
    }

    public void drawMap(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[17]);
        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2d(0, 0);
        gl.glVertex2i(0, 2);
        gl.glTexCoord2d(0, 1);
        gl.glVertex2i(0, boardHeight - 3);
        gl.glTexCoord2d(1, 1);
        gl.glVertex2i(boardWidth, boardHeight - 3);
        gl.glTexCoord2d(1, 0);
        gl.glVertex2i(boardWidth, 2);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    public void saveScore() {
        try (FileWriter fw = new FileWriter(score, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(playerName.getText() + " " + pacman.getScore());
            out.close();
        } catch (IOException e) {

        }
    }

    public void takePlayerName() {
        inputDialog.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
        if (c == 'w' || c == 's' || c == 'd' || c == 'a') {
            direction = c;
        }
        if (e.getKeyCode() == 27) {
            cardController.show(mainPanel, "menu");
            playSound("a.wav");
            if (animate.isAnimating()) {
                animate.stop();
            }
        }
        if(gameOver || gameCompleted) {
            gameOver = false;
            gameCompleted = false;
            returnAllToStartingPoint();
            seconds = 0;
            counter = 0;
            pacman.resetLives();
            pacman.reserScore();
            gameStarted = false;
            try {
                readMap();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
            animate.start();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
