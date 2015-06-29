import javax.swing.*;

import java.awt.*;

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
        int show_menu = Integer.parseInt(config.get("show_menu", "1"));

        setBounds(0, 0, WINDOW_WIDTH + 20, WINDOW_HEIGHT + 40);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(config.get("title", "Game2d-menu"));
        setLocationRelativeTo(null); // centers window
        setResizable(false);

        if (show_menu > 0) {
            GameMenu menu = new GameMenu(this);
            getContentPane().add(menu);
            pack();
        } else {
            startGame();
        }

        setVisible(true);
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
