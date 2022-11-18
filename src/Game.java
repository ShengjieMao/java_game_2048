import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

/*
 * this class sets the logics and algorithms for running the 2048 game. it sets the default appearance
 * of the game, including the size of the window, the output style, etc. it also defines the methods
 * for running the program with accompanies of the GUI actions.
 */
public class Game extends JPanel implements KeyListener, Runnable{
    public static final int width = 400;
    public static final int height = 630;
    private Thread game;
    private boolean running;
    private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    private GameBoard board;

    private long startTime;
    private long elapsed;
    private boolean set;

    public Game(){
        setFocusable(true);
        setPreferredSize(new Dimension(width, height));
        addKeyListener(this);

        board = new GameBoard(width/2 - GameBoard.board_width/2,
                height - GameBoard.board_height - 10);
    }

    private void update(){
        board.update();
        Keyboard.update();

    }

    private void render(){
        // start the loop to draw the image
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        board.render(g);
        // render board
        g.dispose();

        // once the image drawing is done, draw the image to the JPanel of the design
        Graphics2D g2d = (Graphics2D) getGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
    }


    /**
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        Keyboard.keyPressed(e);
    }

    /**
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        Keyboard.keyReleased(e);
    }

    /**
     * the method run will be called when the user starts a thread
     */
    @Override
    public void run() {
        int fps = 0, updates = 0;
        long fpsTimer = System.currentTimeMillis();
        double nsPerUpdate = 1000000000.0 / 60; // keep tracking of how many ns between the updates

        // last update time in nanoseconds
        double then = System.nanoTime();
        double unprocessed = 0;

        while(running) {
            boolean shouldRender = false;
            double now = System.nanoTime();
            unprocessed += (now - then) / nsPerUpdate; // counts update times needed
            then = now; // reset the time

            // update queue
            while (unprocessed >= 1) {
                updates++;
                update();
                unprocessed--;
                shouldRender = true;
            }

            // render
            if (shouldRender) {
                fps++;
                render();
                shouldRender = false;
            } else {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // FPS Timer prints out the update times and frames per seconds
            if (System.currentTimeMillis() - fpsTimer > 1000) {
                System.out.printf("%d fps %d updates,", fps, updates);
                System.out.println();
                fps = 0;
                updates = 0;
                fpsTimer += 1000;
            }
        }
    }
    public synchronized void start(){
        if(running) return;
        running = true;
        game = new Thread(this, "game");
        game.start();
    }

    public synchronized void stop(){
        if(!running) return;
        running = false;
        System.exit(0);
    }
}
