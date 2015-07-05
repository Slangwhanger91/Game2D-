import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class GameWindow extends Application {
    Game2d game_window;
    Group root;

    @Override
    public void start(Stage stage) {
        Settings config;
        final int WINDOW_WIDTH, WINDOW_HEIGHT;

        config = new Settings("config.properties");
        WINDOW_WIDTH = Integer.parseInt(config.get("width", "800"));
        WINDOW_HEIGHT = Integer.parseInt(config.get("height", "600"));
        int show_menu = Integer.parseInt(config.get("show_menu", "1"));

        root = new Group();
        if (show_menu > 0) {
            GameMenu menu = new GameMenu(this, stage);
            stage.setScene(menu.scene);
        } else {
           // TODO: Add no menu option
            stage.setScene(new Scene(root, WINDOW_WIDTH + 20, WINDOW_HEIGHT + 40));
        }

        stage.setTitle(config.get("title", "The amazing adventures of BOX"));
        stage.show();

        //setBounds(0, 0, WINDOW_WIDTH + 20, WINDOW_HEIGHT + 40);
        //setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //setTitle(config.get("title", "Game2d-menu"));
        //setLocationRelativeTo(null); // centers window
        //setResizable(false);

    }

    void startGame(Stage stage) {
        Game2d game = new Game2d();
        stage.setScene(game.scene);
        /*
        getContentPane().removeAll();
        game_window = new Game2d(keyListener);
        game_window.in_panel.addKeyListener(keyListener);
        getContentPane().add(game_window.in_panel);
        validate();
        game_window.gameLoop.start();
        game_window.in_panel.requestFocus();
        */
    }

    public static void main(String[] args) {
        launch(args);
    }
}
