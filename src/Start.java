import javax.swing.*;

/*
 * this class is for starting the 2048 program. It includes the main method for initializing the program
 */
public class Start {
    public static void main(String[] args){
        Game game = new Game();

        // GUI settings
        JFrame window = new JFrame("2048");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.add(game);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // start the game here
        game.start();
    }
}
