/**
 * Created by aperte on 28.06.2015.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame {
    Game2d game_window;
    private static Listener keyListener = new Listener();

    GameWindow() {
        super();
        Settings config;
        final int WINDOW_WIDTH, WINDOW_HEIGHT;

        config = new Settings("config.properties");
        WINDOW_WIDTH = Integer.parseInt(config.get("width", "800"));
        WINDOW_HEIGHT = Integer.parseInt(config.get("height", "600"));

        JButton startButton = new JButton("Start game");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        getContentPane().add(startButton);

        setBounds(0, 0, WINDOW_WIDTH + 20, WINDOW_HEIGHT + 40);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // setBackground(Color.GRAY);
        setTitle(config.get("title", "Game2d-menu"));
        setLocationRelativeTo(null); // centers window
    }

    void startGame() {
        getContentPane().removeAll();
        game_window = new Game2d(keyListener);
        game_window.in_panel.addKeyListener(keyListener);
        getContentPane().add(game_window.in_panel);
        validate();
        game_window.gameLoop.start();
        game_window.in_panel.requestFocus();
    }

    public void start() {
        setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GameWindow window = new GameWindow();
                    window.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
