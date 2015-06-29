import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
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

        /*
        JButton startButton = new JButton("Start game");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        getContentPane().add(startButton);
        */
        GameMenu menu = new GameMenu(this);
        getContentPane().add(menu);
        pack();

        setBounds(0, 0, WINDOW_WIDTH + 20, WINDOW_HEIGHT + 40);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // setBackground(Color.GRAY);
        setTitle(config.get("title", "Game2d-menu"));
        setLocationRelativeTo(null); // centers window
        setVisible(true);
    }

    void startGame() {
        getContentPane().removeAll();
        game_window = new Game2d(keyListener);
        Game2d.in_panel.addKeyListener(keyListener);
        getContentPane().add(Game2d.in_panel);
        validate();
        game_window.gameLoop.start();
        Game2d.in_panel.requestFocus();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    @SuppressWarnings("unused")
					GameWindow window = new GameWindow();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
